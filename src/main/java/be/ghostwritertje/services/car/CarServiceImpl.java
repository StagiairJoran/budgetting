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
    private final CarDao dao;
    private final PersonService personService;

    @Autowired
    public CarServiceImpl(CarDao dao, PersonService personService) {
        this.dao = dao;
        this.personService = personService;
    }

    @Override
    public List<Car> findAll(Person owner) {
        return this.dao.findByOwner(owner);
    }

    @Override
    public Car save(Car car) {
        return this.dao.save(car);
    }

    @Override
    protected CrudRepository<Car, String> getDao() {
        return this.dao;
    }
}
