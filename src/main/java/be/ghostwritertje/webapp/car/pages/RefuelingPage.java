package be.ghostwritertje.webapp.car.pages;

import be.ghostwritertje.domain.car.Refueling;
import be.ghostwritertje.services.car.RefuelingSearchResult;
import be.ghostwritertje.services.car.RefuelingService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.IModelBasedVisibilityBehavior;
import be.ghostwritertje.webapp.LambdaOnChangeBehavior;
import be.ghostwritertje.webapp.form.BaseForm;
import be.ghostwritertje.webapp.form.FormComponentBuilderFactory;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkbox.bootstrapcheckbox.BootstrapCheckBoxPicker;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class RefuelingPage extends BasePage<Refueling> {
    private static final long serialVersionUID = -2645779277878940810L;

    @SpringBean
    private RefuelingService refuelingService;

    private static final String LITERS_ID = "liters";

    private final IModel<RefuelingSearchResult> searchResultIModel = new Model<RefuelingSearchResult>();

    RefuelingPage(IModel<Refueling> model) {
        super(model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        LinkBuilderFactory.pageLink(() -> LambdaModel.of(this.getModel(), Refueling::getCar), RefuelingListPage::new)
                .usingDefaults()
                .attach(this, "back");

        Form<Refueling> form = new BaseForm<Refueling>("form", this.getModel());

        IModel<LocalDate> localDateLambdaModel = LambdaModel.of(this.getModel(), Refueling::getDate, Refueling::setDate);

        FormComponentBuilderFactory.date()
                .usingDefaults()
                .body(new ResourceModel("date"))
                .attach(form, "date", localDateLambdaModel);

        FormComponentBuilderFactory.number(BigDecimal.class)
                .usingDefaults()
                .body(new ResourceModel("kilometres"))
                .attach(form, "kilometres", LambdaModel.of(this.getModel(), Refueling::getKilometres, Refueling::setKilometres))
                .body(new ResourceModel("liters"))
                .attach(form, LITERS_ID, LambdaModel.of(this.getModel(), Refueling::getLiters, Refueling::setLiters))
                .behave(() -> new LambdaOnChangeBehavior(updateMissingField(this.getModel())))
                .body(new ResourceModel("price"))
                .attach(form, "price", LambdaModel.of(this.getModel(), Refueling::getPrice, Refueling::setPrice))
                .body(new ResourceModel("liter.price"))
                .attach(form, "literPrice", LambdaModel.of(this.getModel(), Refueling::getPricePerLiter, Refueling::setPricePerLiter));

        CheckBox checkBox = new BootstrapCheckBoxPicker("tankFull", LambdaModel.of(this.getModel(), Refueling::isFuelTankFull, Refueling::setFuelTankFull));
        form.add(checkBox);

        LinkBuilderFactory.submitLink(save())
                .usingDefaults()
                .attach(form, "save");

        WebMarkupContainer savedinfoContainer = new WebMarkupContainer("result");
        savedinfoContainer.setOutputMarkupId(true);
        savedinfoContainer.add(new IModelBasedVisibilityBehavior<>(this.searchResultIModel, Objects::nonNull));

        FormComponentBuilderFactory.number(BigDecimal.class)
                .usingDefaults()
                .body(new ResourceModel("consumption"))
                .attach(savedinfoContainer, "consumption", LambdaModel.of(this.searchResultIModel, RefuelingSearchResult::getConsumption));

        FormComponentBuilderFactory.number(BigDecimal.class)
                .usingDefaults()
                .body(new ResourceModel("average.distance.year"))
                .attach(savedinfoContainer, "averageDistance", LambdaModel.of(this.searchResultIModel, RefuelingSearchResult::getKilometresPerYear));

        form.add(savedinfoContainer);
        this.add(form);
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxSubmitLink> save() {
        return (target, components) -> {
            RefuelingPage parent = components.findParent(RefuelingPage.class);
            Refueling saved = parent.refuelingService.save(parent.getModelObject());
            parent.setModelObject(saved);
            parent.searchResultIModel.setObject(parent.refuelingService.mapRefuelingToSearchResult(saved));
        };
    }

    private Component getLiters() {
        return this.get("form").get(LITERS_ID);
    }

    private static SerializableBiConsumer<Component, AjaxRequestTarget> updateMissingField(IModel<Refueling> refuelingModel) {
        return (component, ajaxRequestTarget) -> {
            RefuelingPage parent = component.findParent(RefuelingPage.class);
            Refueling refueling = refuelingModel.getObject();
            BigDecimal pricePerLiter = refueling.getPricePerLiter();
            BigDecimal price = refueling.getPrice();

            if (pricePerLiter.compareTo(BigDecimal.ZERO) != 0 && price.compareTo(BigDecimal.ZERO) != 0) {
                refueling.setLiters(price.divide(pricePerLiter, RoundingMode.HALF_DOWN));
                ajaxRequestTarget.add(parent.getLiters());
            }

        };
    }

}
