package be.ghostwritertje.services.car;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.car.Car;
import be.ghostwritertje.repository.CarDao;
import be.ghostwritertje.services.DomainObjectCrudServiceSupport;
import be.ghostwritertje.services.person.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Service
public class CarServiceImpl extends DomainObjectCrudServiceSupport<Car> implements CarService {
    @Autowired
    private CarDao dao;

    @Autowired
    private PersonService personService;

    @Override
    public List<Car> findAll(Person owner) {
        return this.dao.findByOwner(owner);
    }

    @Override
    public Car save(Car car) {
        return this.dao.save(car);
    }

    @Override
    protected CrudRepository<Car, Integer> getDao() {
        return this.dao;
    }
}
