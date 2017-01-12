package be.ghostwritertje.webapp.car.pages;

import be.ghostwritertje.domain.car.Refueling;
import be.ghostwritertje.services.car.RefuelingService;
import be.ghostwritertje.utilities.NumberUtilities;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.LambdaOnChangeBehavior;
import be.ghostwritertje.webapp.form.BaseForm;
import be.ghostwritertje.webapp.form.FormComponentBuilderFactory;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkbox.bootstrapcheckbox.BootstrapCheckBoxPicker;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.lambda.WicketBiConsumer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.time.LocalDate;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class RefuelingPage extends BasePage<Refueling> {

    @SpringBean
    private RefuelingService refuelingService;

    private final String LITERS_ID = "liters";

    protected RefuelingPage(IModel<Refueling> model) {
        super(model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        Form<Refueling> form = new BaseForm<Refueling>("form", this.getModel()) {
            @Override
            public void onSubmit() {
                super.onSubmit();
            }
        };

        IModel<LocalDate> localDateLambdaModel = LambdaModel.of(this.getModel(), Refueling::getDate, Refueling::setDate);

        FormComponentBuilderFactory.date()
                .usingDefaults()
                .body(new ResourceModel("date"))
                .attach(form, "date", localDateLambdaModel);

        FormComponentBuilderFactory.number()
                .usingDefaults()
                .body(new ResourceModel("kilometres"))
                .attach(form, "kilometres", new LambdaModel<>(() -> this.getModelObject().getKilometres(), kilometres -> this.getModelObject().setKilometres(kilometres)))
                .body(new ResourceModel("liters"))
                .attach(form, LITERS_ID, new LambdaModel<Double>(() -> this.getModelObject().getLiters(), liters -> this.getModelObject().setLiters(liters)))
                .behave(() -> new LambdaOnChangeBehavior(updateMissingField(this.getModel())))
                .body(new ResourceModel("price"))
                .attach(form, "price", new LambdaModel<Double>(() -> this.getModelObject().getPrice(), price -> this.getModelObject().setPrice(price)))
                .body(new ResourceModel("liter.price"))
                .attach(form, "literPrice", new LambdaModel<Double>(() -> this.getModelObject().getPricePerLiter(), kilometres -> this.getModelObject().setPricePerLiter(kilometres)));

        CheckBox checkBox = new BootstrapCheckBoxPicker("tankFull", new LambdaModel<>(() -> this.getModelObject().isFuelTankFull(), full -> this.getModelObject().setFuelTankFull(full)));
        form.add(checkBox);

        LinkBuilderFactory.submitLink(save())
                .usingDefaults()
                .attach(form, "save");

        this.add(form);
    }

    private static WicketBiConsumer<AjaxRequestTarget, AjaxSubmitLink> save() {
        return (target, components) -> {
            RefuelingPage parent = components.findParent(RefuelingPage.class);
            parent.refuelingService.save(parent.getModelObject());
        };
    }

    private Component getLiters() {
        return this.get("form").get(LITERS_ID);
    }

    private static WicketBiConsumer<Component, AjaxRequestTarget> updateMissingField(IModel<Refueling> refuelingModel) {
        return (component, ajaxRequestTarget) -> {
            RefuelingPage parent = component.findParent(RefuelingPage.class);
            Refueling refueling = refuelingModel.getObject();
            Double pricePerLiter = refueling.getPricePerLiter();
            Double price = refueling.getPrice();

            if (pricePerLiter != 0 && price != 0) {
                refueling.setLiters(NumberUtilities.round(price / pricePerLiter, 2));
                ajaxRequestTarget.add(parent.getLiters());
            }

        };
    }

}
