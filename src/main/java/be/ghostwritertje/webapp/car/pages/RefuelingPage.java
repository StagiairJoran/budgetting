package be.ghostwritertje.webapp.car.pages;

import be.ghostwritertje.domain.car.Refueling;
import be.ghostwritertje.services.car.RefuelingService;
import be.ghostwritertje.utilities.NumberUtilities;
import be.ghostwritertje.webapp.form.BaseForm;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.LambdaOnChangeBehavior;
import be.ghostwritertje.webapp.LocalDateTextField;
import be.ghostwritertje.webapp.car.model.CarModel;
import be.ghostwritertje.webapp.form.FormComponentBuilderFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.lambda.WicketBiConsumer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.Model;
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
                Refueling savedRefueling = RefuelingPage.this.refuelingService.save(RefuelingPage.this.getModelObject());
                this.setResponsePage(new RefuelingListPage(new CarModel(new Model<Integer>(savedRefueling.getCar().getId()))));
            }
        };

        IModel<LocalDate> localDateLambdaModel = new LambdaModel<>(() -> this.getModel().getObject().getDate(), localDate -> this.getModel().getObject().setDate(localDate));
        form.add(new LocalDateTextField("date", localDateLambdaModel));
        form.add(new NumberTextField<Double>("kilometres", new LambdaModel<Double>(() -> this.getModelObject().getKilometres(), kilometres -> this.getModelObject().setKilometres(kilometres)), Double.class));


        NumberTextField<Double> litersField = new NumberTextField<>(LITERS_ID, new LambdaModel<Double>(() -> this.getModelObject().getLiters(), liters -> this.getModelObject().setLiters(liters)), Double.class);
        litersField.add(new LambdaOnChangeBehavior(updateMissingField(this.getModel())));
        form.add(litersField);

        CheckBox checkBox = new CheckBox("tankFull", new LambdaModel<>(() -> this.getModelObject().isFuelTankFull(), full -> this.getModelObject().setFuelTankFull(full)));
        form.add(checkBox);

        NumberTextField<Double> priceField = new NumberTextField<>("price", new LambdaModel<Double>(() -> this.getModelObject().getPrice(), price -> this.getModelObject().setPrice(price)), Double.class);
        priceField.add(new LambdaOnChangeBehavior(updateMissingField(this.getModel())));

        form.add(priceField);


        NumberTextField<Double> literPriceField = new NumberTextField<>("literPrice", new LambdaModel<Double>(() -> this.getModelObject().getPricePerLiter(), kilometres -> this.getModelObject().setPricePerLiter(kilometres)), Double.class);
        literPriceField.add(new LambdaOnChangeBehavior(updateMissingField(this.getModel())));

        form.add(literPriceField);

        form.add(new SubmitLink("save"));

        this.add(form);
    }

    private Component getLiters(){
        return this.get("form").get(LITERS_ID);
    }

    private static WicketBiConsumer<Component, AjaxRequestTarget> updateMissingField(IModel<Refueling> refuelingModel) {
        return (component, ajaxRequestTarget) -> {
            RefuelingPage parent = component.findParent(RefuelingPage.class);
            Refueling refueling = refuelingModel.getObject();
            Double pricePerLiter = refueling.getPricePerLiter();
            Double price = refueling.getPrice();

            if (pricePerLiter != 0 && price != 0) {
                refueling.setLiters(NumberUtilities.round(price / pricePerLiter,2));
                ajaxRequestTarget.add(parent.getLiters());
            }

        };
    }

}
