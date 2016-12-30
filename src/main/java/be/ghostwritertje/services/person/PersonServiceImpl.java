package be.ghostwritertje.services.person;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.repository.PersonDao;
import be.ghostwritertje.services.DomainObjectCrudServiceSupport;
import be.ghostwritertje.utilities.PasswordUtility;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ghostwritertje
 * Date: 29-Sep-16.
 */
@Service
public class PersonServiceImpl extends DomainObjectCrudServiceSupport<Person> implements PersonService {
    private final PersonDao dao;
    private static final Logger logger = Logger.getLogger(PersonServiceImpl.class);

    @Autowired
    public PersonServiceImpl(PersonDao dao) {
        this.dao = dao;
    }

    @Override
    public String getLoggedInUser() {
        return this.dao.findByUsername("Ghostwritertje").getUsername();
    }

    @Override
    public List<Person> findAll() {
        logger.info("Getting all users");
        Iterable<Person> userIterable = this.dao.findAll();

        List<Person> personList = new ArrayList<>();
        userIterable.forEach(personList::add);

        return personList;
    }

    @Override
    public Person findByUsername(String username) {
        return this.dao.findByUsername(username);
    }

    @Override
    public Person save(Person person) {
        person.setPassword(PasswordUtility.hashPassword(person.getPassword()));
        return this.dao.save(person);
    }

    @Override
    public Person logIn(Person person) {
        return this.dao.findByUsernameAndPassword(person.getUsername(), PasswordUtility.hashPassword(person.getPassword()));
    }

    @Override
    protected CrudRepository<Person, String> getDao() {
        return this.dao;
    }
}
