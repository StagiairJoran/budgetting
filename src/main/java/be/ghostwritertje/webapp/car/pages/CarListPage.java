package be.ghostwritertje.webapp.car.pages;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.car.Car;
import be.ghostwritertje.domain.car.Refueling;
import be.ghostwritertje.services.car.CarService;
import be.ghostwritertje.webapp.BasePage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.time.LocalDate;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class CarListPage extends BasePage<Person> {

    @SpringBean
    private CarService carService;

    public CarListPage(IModel<Person> model) {
        super(model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        this.add(new ListView<Car>("cars", this.carService.findAll(this.getModelObject())) {

            @Override
            protected void populateItem(ListItem<Car> item) {
                item.add(new Link<Person>("refuelingsLink") {
                    @Override
                    protected void onInitialize() {
                        super.onInitialize();
                        this.add(new Label("brand", item.getModelObject().getBrand()));
                    }

                    @Override
                    public void onClick() {
                        setResponsePage(new RefuelingListPage(item.getModel()));
                    }
                });
                item.add(new Label("model", item.getModelObject().getModel()));
                item.add(new Label("purchaseDate", item.getModelObject().getPurchaseDate()));

            }
        });


        this.add(new Link<Refueling>("newCarLink") {
            @Override
            public void onClick() {
                Car car = new Car();
                car.setOwner(CarListPage.this.getModelObject());
                car.setPurchaseDate(LocalDate.now());
                this.setResponsePage(new CarPage(new Model<Car>(car)));
            }
        });

    }
}
