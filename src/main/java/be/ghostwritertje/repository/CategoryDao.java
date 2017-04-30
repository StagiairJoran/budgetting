package be.ghostwritertje.repository;

import be.ghostwritertje.domain.budgetting.Category;
import be.ghostwritertje.domain.budgetting.CategoryGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 17-Apr-17.
 */
@Repository
public interface CategoryDao  extends CrudRepository<Category, String> {

    List<Category> findByCategoryGroup(CategoryGroup categoryGroup);
}
