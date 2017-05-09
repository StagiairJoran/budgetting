package be.ghostwritertje.services.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.CategoryType;
import be.ghostwritertje.repository.CategoryGroupViewDao;
import be.ghostwritertje.views.budgetting.CategoryGroupView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Jorandeboever on 5/9/2017.
 */
@Service
public class CategoryGroupViewServiceImpl implements CategoryGroupViewService {

    private final CategoryGroupViewDao dao;

    @Autowired
    public CategoryGroupViewServiceImpl(CategoryGroupViewDao dao) {
        this.dao = dao;
    }

    @Override
    public List<CategoryGroupView> findByAdministratorOrderByName(Person administrator, CategoryType categoryType) {
        return this.dao.findByAdministratorAndCategoryTypeOrderByName(administrator, categoryType);
    }
}
