package be.ghostwritertje.services.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.Category;
import be.ghostwritertje.domain.budgetting.CategoryGroup;
import be.ghostwritertje.domain.budgetting.CategoryType;
import be.ghostwritertje.services.DomainObjectCrudService;
import be.ghostwritertje.services.NumberDisplay;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 17-Apr-17.
 */
public interface CategoryService extends DomainObjectCrudService<Category> {
    List<Category> findByCategoryGroup(CategoryGroup categoryGroup);

    List<Category> findByAdministrator(Person administrator);

    List<NumberDisplay> findCountByAdministrator(Person administrator);

    List<NumberDisplay> findSumByAdministrator(Person administrator, CategoryType categoryType);

    Iterable<Category> save(Iterable<Category> categories);

    void initForNewPerson(Person person);

    void attemptToAssignCategoriesAutomaticallyForPerson(Person person);
}
