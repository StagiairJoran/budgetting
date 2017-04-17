package be.ghostwritertje.repository;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 17-Apr-17.
 */
@Repository
public interface CategoryDao  extends CrudRepository<Category, String> {

    List<Category> findByAdministrator(Person administrator);
}
