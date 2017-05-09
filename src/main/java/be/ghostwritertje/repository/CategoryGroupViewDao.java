package be.ghostwritertje.repository;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.CategoryType;
import be.ghostwritertje.views.budgetting.CategoryGroupView;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Jorandeboever on 5/9/2017.
 */
public interface CategoryGroupViewDao extends CrudRepository<CategoryGroupView, String> {

    List<CategoryGroupView> findByAdministratorAndCategoryTypeOrderByName(Person administrator, CategoryType categoryType);
}
