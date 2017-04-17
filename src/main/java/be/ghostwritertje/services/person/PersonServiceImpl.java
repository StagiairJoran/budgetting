package be.ghostwritertje.services.person;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.repository.PersonDao;
import be.ghostwritertje.services.DomainObjectCrudServiceSupport;
import be.ghostwritertje.services.budgetting.CategoryService;
import be.ghostwritertje.utilities.PasswordUtility;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ghostwritertje
 * Date: 29-Sep-16.
 */
@Service
public class PersonServiceImpl extends DomainObjectCrudServiceSupport<Person> implements PersonService {
    private final PersonDao dao;

    private final CategoryService categoryService;

    private static final Logger logger = Logger.getLogger(PersonServiceImpl.class);

    @Autowired
    public PersonServiceImpl(PersonDao dao, CategoryService categoryService) {
        this.dao = dao;
        this.categoryService = categoryService;
    }

    @PostConstruct
    public void init(){
        List<Person> people = this.findAll();
        people.forEach(this.categoryService::initForNewPerson);
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
        return StringUtils.isEmpty(person.getUuid()) ? this.create(person) : this.update(person);
    }

    private Person create(Person person){
        person.setPassword(PasswordUtility.hashPassword(person.getPassword()));
        person = this.dao.save(person);
        this.categoryService.initForNewPerson(person);
        return person;
    }

    private Person update(Person person){
        person = this.dao.save(person);
        return person;
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
