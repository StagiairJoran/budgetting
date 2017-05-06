package be.ghostwritertje.webapp;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.car.Car;
import be.ghostwritertje.services.budgetting.CategoryService;
import be.ghostwritertje.services.car.CarService;
import be.ghostwritertje.webapp.car.panel.CarInfoPanel;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import be.ghostwritertje.webapp.model.BankAccountListInfoPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 02-Oct-16.
 */
public class DashboardPage extends BasePage<Person> {
    @SpringBean
    private CarService carService;

    @SpringBean
    private CategoryService categoryService;

    public DashboardPage() {
        super(new Model<>(CustomSession.get().getLoggedInPerson()));
    }

    public DashboardPage(IModel<Person> model) {
        super(model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        List<Car> carList = this.carService.findAll(this.getModelObject());

        this.add(new ListView<Car>("carListView", carList) {
            @Override
            protected void populateItem(ListItem<Car> item) {
                item.add(new CarInfoPanel("carInfo", item.getModel()));
            }
        });

        this.add(new BankAccountListInfoPanel("bankAccountView", this.getModel()));

    }


}
