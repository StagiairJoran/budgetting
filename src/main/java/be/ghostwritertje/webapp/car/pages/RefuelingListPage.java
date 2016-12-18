package be.ghostwritertje.webapp.car.pages;

import be.ghostwritertje.domain.car.Car;
import be.ghostwritertje.domain.car.Refueling;
import be.ghostwritertje.services.car.RefuelingService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.car.panel.CarInfoPanel;
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
public class RefuelingListPage extends BasePage<Car> {

    @SpringBean
    private RefuelingService refuelingService;

    public RefuelingListPage(IModel<Car> model) {
        super(model);
    }


    @Override
    protected void onInitialize() {
        super.onInitialize();

        this.add(new CarInfoPanel("carInfo", this.getModel()));

        this.add(new ListView<Refueling>("refuelings", this.refuelingService.findByCar(this.getModelObject())) {

            @Override
            protected void populateItem(ListItem<Refueling> item) {
                item.add(new Label("date", item.getModelObject().getDate()));
                item.add(new Label("kilometres", item.getModelObject().getKilometres()));
                item.add(new Label("liters", item.getModelObject().getLiters()));
                item.add(new Label("price", item.getModelObject().getPrice()));
                item.add(new Label("pricePerLiter", item.getModelObject().getPricePerLiter()));

                item.add(new Link<Refueling>("edit", item.getModel()) {
                    @Override
                    public void onClick() {
                        this.setResponsePage(new RefuelingPage(this.getModel()));
                    }
                });
                item.add(new Link<Car>("delete", RefuelingListPage.this.getModel()) {
                    @Override
                    public void onClick() {
                        RefuelingListPage.this.refuelingService.delete(item.getModelObject());
                        this.setResponsePage(new RefuelingListPage(this.getModel()));
                    }
                });
            }
        });

        this.add(new Link<Refueling>("newRefuelingLink") {
            @Override
            public void onClick() {
                Refueling refueling = new Refueling();
                refueling.setCar(RefuelingListPage.this.getModelObject());
                refueling.setDate(LocalDate.now());
                this.setResponsePage(new RefuelingPage(new Model<Refueling>(refueling)));
            }
        });
    }
}
