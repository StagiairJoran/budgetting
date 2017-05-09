package be.ghostwritertje.services.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.CategoryType;
import be.ghostwritertje.views.budgetting.CategoryGroupView;

import java.util.List;

/**
 * Created by Jorandeboever on 5/9/2017.
 */
public interface CategoryGroupViewService {
    List<CategoryGroupView> findByAdministratorOrderByName(Person administrator, CategoryType categoryType);
}
