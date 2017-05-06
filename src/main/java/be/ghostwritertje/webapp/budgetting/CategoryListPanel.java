package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.Category;
import be.ghostwritertje.domain.budgetting.CategoryGroup;
import be.ghostwritertje.services.budgetting.CategoryGroupService;
import be.ghostwritertje.services.budgetting.CategoryService;
import be.ghostwritertje.webapp.charts.ChartBuilderFactory;
import be.ghostwritertje.webapp.datatable.ColumnBuilderFactory;
import be.ghostwritertje.webapp.datatable.DataTableBuilderFactory;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import be.ghostwritertje.webapp.model.DomainObjectListModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;

import java.util.List;

/**
 * Created by Jorandeboever on 5/6/2017.
 */
public class CategoryListPanel extends GenericPanel<Person> {

    private static final long serialVersionUID = -1312028287033905928L;
    @SpringBean
    private CategoryGroupService categoryGroupService;

    @SpringBean
    private CategoryService categoryService;

    private final IModel<List<CategoryGroup>> categoryGroupListModel;

    public CategoryListPanel(String id, IModel<Person> model) {
        super(id, model);
        this.categoryGroupListModel = new DomainObjectListModel<CategoryGroup, CategoryGroupService>(
                this.categoryGroupService,
                categoryGroupCategoryGroupService -> categoryGroupCategoryGroupService.findByAdministrator(this.getModelObject())
        );
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        DataTableBuilderFactory.<CategoryGroup, String>simple()
                .addColumn(ColumnBuilderFactory.<CategoryGroup, String>simple(CategoryGroup::getName).build(new ResourceModel("category")))
                .addColumn(ColumnBuilderFactory.actions(new ResourceModel("actions"), editCategoryGroup(), deleteCategoryGroup()))
                .attach(this, "dataTable", this.categoryGroupListModel);


        LinkBuilderFactory.ajaxLink(assignCategories())
                .usingDefaults()
                .body(new ResourceModel("assign.categories.automatically"))
                .attach(this, "assignCategories");

        ChartBuilderFactory.pieChart()
                .title("Categories")
                .name("Statements")
                .addPoints(this.categoryService.findCountByAdministrator(this.getModelObject()), Category::getName, aLong -> aLong)
                .attach(this, "pieChart");

    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxLink<CategoryGroup>> deleteCategoryGroup() {
        return (ajaxRequestTarget, components) -> {
            CategoryListPanel parent = components.findParent(CategoryListPanel.class);
            parent.categoryGroupService.delete(components.getModelObject());
            parent.categoryGroupListModel.setObject(null);
            ajaxRequestTarget.add(parent);
        };
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxLink<CategoryGroup>> editCategoryGroup() {
        return (ajaxRequestTarget, components) -> {
            components.setResponsePage(new CategoryGroupPage(components.getModel()));
        };
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxLink<Object>> assignCategories() {
        return (ajaxRequestTarget, components) -> {
            CategoryListPanel parent = components.findParent(CategoryListPanel.class);
            parent.categoryService.attemptToAssignCategoriesAutomaticallyForPerson(parent.getModelObject());
            ajaxRequestTarget.add(parent);
        };
    }
}
