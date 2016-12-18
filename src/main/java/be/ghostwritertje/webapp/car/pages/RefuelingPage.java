package be.ghostwritertje.webapp.car.pages;

import be.ghostwritertje.domain.car.Refueling;
import be.ghostwritertje.services.car.RefuelingService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.LocalDateTextField;
import be.ghostwritertje.webapp.car.model.CarModel;
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

    protected RefuelingPage(IModel<Refueling> model) {
        super(model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        Form<Refueling> form = new Form<Refueling>("form", this.getModel()) {
            @Override
            public void onSubmit() {
                super.onSubmit();
                Refueling savedRefueling = RefuelingPage.this.refuelingService.save(RefuelingPage.this.getModelObject());
                this.setResponsePage(new RefuelingListPage(new CarModel(new Model<Integer>(savedRefueling.getCar().getId()))));
            }
        };

        IModel<LocalDate> localDateLambdaModel = new LambdaModel<>(() -> this.getModel().getObject().getDate(), localDate -> this.getModel().getObject().setDate(localDate));
        form.add(new LocalDateTextField("date", localDateLambdaModel));
        form.add(new NumberTextField<Double>("liters", new LambdaModel<Double>(() -> this.getModelObject().getLiters(), liters -> this.getModelObject().setLiters(liters)), Double.class));
        form.add(new NumberTextField<Double>("price", new LambdaModel<Double>(() -> this.getModelObject().getPrice(), price -> this.getModelObject().setPrice(price)), Double.class));
        form.add(new NumberTextField<Double>("kilometres", new LambdaModel<Double>(() -> this.getModelObject().getKilometres(), kilometres -> this.getModelObject().setKilometres(kilometres)), Double.class));

        form.add(new SubmitLink("save"));

        this.add(form);
    }

}
