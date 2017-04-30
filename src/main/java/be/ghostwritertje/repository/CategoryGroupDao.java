package be.ghostwritertje.repository;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.CategoryGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 30-Apr-17.
 */
public interface CategoryGroupDao extends CrudRepository<CategoryGroup, String> {

    List<CategoryGroup> findByAdministrator(Person administrator);
}
