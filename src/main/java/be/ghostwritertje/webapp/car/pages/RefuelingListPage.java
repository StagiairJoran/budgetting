package be.ghostwritertje.webapp.car.pages;

import be.ghostwritertje.domain.car.Car;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.car.panel.CarInfoPanel;
import org.apache.wicket.model.IModel;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class RefuelingListPage extends BasePage<Car> {
    private static final long serialVersionUID = 2574375256093576667L;

    public RefuelingListPage(IModel<Car> model) {
        super(model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        this.add(new CarInfoPanel("carInfo", this.getModel()));

        this.add(new RefuelingListPanel("refuelings", this.getModel()));

        this.add(new RefuelingChartsPanel("charts", this.getModel()));
    }
}
