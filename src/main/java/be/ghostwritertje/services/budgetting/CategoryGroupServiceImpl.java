package be.ghostwritertje.services.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.CategoryGroup;
import be.ghostwritertje.repository.CategoryGroupDao;
import be.ghostwritertje.services.DomainObjectCrudServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 30-Apr-17.
 */
@Service
public class CategoryGroupServiceImpl extends DomainObjectCrudServiceSupport<CategoryGroup> implements CategoryGroupService {

    private final CategoryGroupDao categoryGroupDao;

    @Autowired
    public CategoryGroupServiceImpl(CategoryGroupDao categoryGroupDao) {
        this.categoryGroupDao = categoryGroupDao;
    }

    @Override
    protected CrudRepository<CategoryGroup, String> getDao() {
        return this.categoryGroupDao;
    }

    @Override
    public List<CategoryGroup> findByAdministrator(Person administrator) {
        return this.categoryGroupDao.findByAdministrator(administrator);
    }
}
