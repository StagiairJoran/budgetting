package be.ghostwritertje.services.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.Category;
import be.ghostwritertje.repository.CategoryDao;
import be.ghostwritertje.services.DomainObjectCrudServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 17-Apr-17.
 */
@Service
public class CategoryServiceImpl extends DomainObjectCrudServiceSupport<Category> implements CategoryService {

    private final CategoryDao categoryDao;

    @Autowired
    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public void initForNewPerson(Person person) {
        Collection<Category> categories = new ArrayList<>();
        categories.add(new Category("Saving"));
        categories.add(new Category("Car"));
        categories.add(new Category("House"));
        categories.add(new Category("Groceries"));
        categories.add(new Category("Entertainment"));
        categories.add(new Category("Internal"));

        categories.forEach(category -> category.setAdministrator(person));

        this.save(categories);
    }

    @Override
    public void delete(Category object) {
        this.categoryDao.delete(object);
    }

    @Override
    public Category save(Category object) {
        return this.categoryDao.save(object);
    }

    @Override
    public List<Category> findByAdministrator(Person administrator) {
        return this.categoryDao.findByAdministrator(administrator);
    }

    @Override
    public Iterable<Category> save(Iterable<Category> categories) {
        return this.categoryDao.save(categories);
    }

    @Override
    protected CrudRepository<Category, String> getDao() {
        return this.categoryDao;
    }
}
