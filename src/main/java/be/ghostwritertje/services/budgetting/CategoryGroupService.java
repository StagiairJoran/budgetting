package be.ghostwritertje.services.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.CategoryGroup;
import be.ghostwritertje.services.DomainObjectCrudService;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 30-Apr-17.
 */
public interface CategoryGroupService extends DomainObjectCrudService<CategoryGroup> {
    List<CategoryGroup> findByAdministrator(Person administrator);

}
