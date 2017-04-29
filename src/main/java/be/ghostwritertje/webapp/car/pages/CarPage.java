package be.ghostwritertje.webapp.car.pages;

import be.ghostwritertje.domain.car.Car;
import be.ghostwritertje.services.car.CarService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.LocalDateTextField;
import be.ghostwritertje.webapp.form.BaseForm;
import be.ghostwritertje.webapp.form.FormComponentBuilderFactory;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

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
                .attach(form, "brand", LambdaModel.of(this.getModel(), Car::getBrand, Car::setBrand))
                .body(new ResourceModel("model"))
                .attach(form, "model", LambdaModel.of(this.getModel(), Car::getModel, Car::setModel));

        form.add(new NumberTextField<Double>(
                "price",
                LambdaModel.of(this.getModel(), Car::getPurchasePrice, Car::setPurchasePrice),
                Double.class
        ));
        form.add(new LocalDateTextField(
                "date",
                LambdaModel.of(this.getModel(), Car::getPurchaseDate, Car::setPurchaseDate)
        ));

        LinkBuilderFactory.submitLink((target, o) -> CarPage.this.carService.save(CarPage.this.getModelObject()))
                .usingDefaults()
                .attach(form, "save");


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
