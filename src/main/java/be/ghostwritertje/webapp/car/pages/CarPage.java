package be.ghostwritertje.webapp.car.pages;

import be.ghostwritertje.domain.car.Car;
import be.ghostwritertje.services.car.CarService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.LocalDateTextField;
import be.ghostwritertje.webapp.form.BaseForm;
import be.ghostwritertje.webapp.form.FormComponentBuilderFactory;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import be.ghostwritertje.webapp.person.PersonModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.time.LocalDate;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class CarPage extends BasePage<Car> {

    @SpringBean
    private CarService carService;

    public CarPage(IModel<Car> model) {
        super(model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        BaseForm<Car> form = new BaseForm<>("form", this.getModel());

        FormComponentBuilderFactory.textField()
                .usingDefaults()
                .body(new ResourceModel("brand"))
                .attach(form, "brand", new LambdaModel<String>(() -> this.getModelObject().getBrand(), brand -> this.getModelObject().setBrand(brand)))
                .body(new ResourceModel("model"))
                .attach(form, "model", new LambdaModel<String>(() -> this.getModelObject().getModel(), model -> this.getModelObject().setModel(model)));

        form.add(new NumberTextField<Double>("price", new LambdaModel<>(() -> this.getModelObject().getPurchasePrice(), model -> this.getModelObject().setPurchasePrice(model)), Double.class));
        form.add(new LocalDateTextField("date", new LambdaModel<LocalDate>(() -> this.getModelObject().getPurchaseDate(), date -> this.getModelObject().setPurchaseDate(date))));

        LinkBuilderFactory.submitLink()
                .usingDefaults()
                .attach(form, "save", (target, o) -> CarPage.this.carService.save(CarPage.this.getModelObject()));


        form.add(new AjaxLink<String>("edit") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                form.getFormModeModel().setObject(BaseForm.FormMode.EDIT);
                target.add(form);
            }

        });

        this.add(form);
    }
}
