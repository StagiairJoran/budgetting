package be.ghostwritertje.services.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.Category;
import be.ghostwritertje.services.DomainObjectCrudService;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 17-Apr-17.
 */
public interface CategoryService extends DomainObjectCrudService<Category> {
    List<Category> findByAdministrator(Person administrator);

    Iterable<Category> save(Iterable<Category> categories);

    void initForNewPerson(Person person);
}
