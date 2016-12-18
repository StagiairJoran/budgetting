package be.ghostwritertje.services.car;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.car.Car;
import be.ghostwritertje.services.DomainObjectCrudService;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public interface CarService extends DomainObjectCrudService<Car> {
    List<Car> findAll(Person person);

    Car save(Car car);
}
