package be.ghostwritertje.repository;

import be.ghostwritertje.views.budgetting.CategoryView;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Jorandeboever on 5/9/2017.
 */
public interface CategoryViewDao extends CrudRepository<CategoryView, String> {
}
