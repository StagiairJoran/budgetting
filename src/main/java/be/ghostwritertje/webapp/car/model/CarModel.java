package be.ghostwritertje.webapp.car.model;

import be.ghostwritertje.domain.car.Car;
import be.ghostwritertje.services.car.CarService;
import be.ghostwritertje.webapp.model.LoadableModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class CarModel extends LoadableModel<Car> {

    @SpringBean
    private CarService carService;

    private final IModel<Integer> idModel;

    public CarModel(IModel<Integer> idModel) {
        super();
        this.idModel = idModel;
    }

    @Override
    protected Car load() {
        return this.carService.findOne(this.idModel.getObject());
    }
}
