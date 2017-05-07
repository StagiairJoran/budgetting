package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.budgetting.Category;
import be.ghostwritertje.domain.budgetting.CategoryGroup;
import be.ghostwritertje.domain.budgetting.CategoryType;
import be.ghostwritertje.services.budgetting.CategoryGroupService;
import be.ghostwritertje.services.budgetting.CategoryService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.IModelBasedVisibilityBehavior;
import be.ghostwritertje.webapp.datatable.ColumnBuilderFactory;
import be.ghostwritertje.webapp.datatable.DataTableBuilderFactory;
import be.ghostwritertje.webapp.form.BaseForm;
import be.ghostwritertje.webapp.form.FormComponentBuilderFactory;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.*;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Jorandeboever on 5/6/2017.
 */
public class CategoryGroupPage extends BasePage<CategoryGroup> {
    private static final long serialVersionUID = 7180944468528176393L;

    private static final String FORM_ID = "form";
    private static final String CATEGORY_FORM_ID = "categoryForm";
    @SpringBean
    private CategoryGroupService categoryGroupService;

    @SpringBean
    private CategoryService categoryService;

    private final IModel<Category> toeTeVoegenCategoryModel = new Model<>();

    public CategoryGroupPage(IModel<CategoryGroup> model) {
        super(model);
    }


    @Override
    protected void onInitialize() {
        super.onInitialize();

        this.add(new Label("title", new StringResourceModel("title.category.group", LambdaModel.of(this.getModel(), CategoryGroup::getName))));

        BaseForm<CategoryGroup> form = new BaseForm<>(FORM_ID, this.getModel());

        FormComponentBuilderFactory.textField()
                .usingDefaults()
                .attach(form, "name", LambdaModel.of(this.getModel(), CategoryGroup::getName, CategoryGroup::setName));

        IModel<CategoryType> categoryTypeIModel = LambdaModel.<CategoryGroup, CategoryType>of(this.getModel(), CategoryGroup::getCategoryType, CategoryGroup::setCategoryType);
        FormComponentBuilderFactory.<CategoryType>dropDown()
                .usingDefaults()
                .body(new ResourceModel("category.type"))
                .attach(
                        form,
                        "categoryType",
                        categoryTypeIModel,
                        new ListModel<>(Arrays.asList(CategoryType.EXPENSE, CategoryType.INCOME))
                );


        LinkBuilderFactory.submitLink(addCategory())
                .usingDefaults()
                .body(new ResourceModel("name"))
                .body(new ResourceModel("add.category"))
                .attach(form, "addCategory");

        BaseForm<Category> categoryForm = new BaseForm<>(CATEGORY_FORM_ID, this.toeTeVoegenCategoryModel);
        categoryForm.add(new IModelBasedVisibilityBehavior<Category>(this.toeTeVoegenCategoryModel, Objects::nonNull));

        FormComponentBuilderFactory.textField()
                .usingDefaults()
                .body(new ResourceModel("category.name"))
                .attach(categoryForm, "categoryName", LambdaModel.of(this.toeTeVoegenCategoryModel, Category::getName, Category::setName));

        LinkBuilderFactory.submitLink(saveCategory())
                .usingDefaults()
                .body(new ResourceModel("save.category"))
                .attach(categoryForm, "saveCategory");

        form.add(categoryForm);

        DataTableBuilderFactory.<Category, String>simple()
                .addColumn(ColumnBuilderFactory.<Category, String>simple(Category::getName).build(new ResourceModel("name")))
                .addColumn(ColumnBuilderFactory.custom(
                        new ResourceModel("delete"),
                        (s, categoryIModel) -> LinkBuilderFactory.ajaxLink(deleteCategory())
                                .usingDefaults()
                                .body(new ResourceModel("delete"))
                                .build(s, categoryIModel)))
                .attach(form, "categories", LambdaModel.of(this.getModel(), CategoryGroup::getCategoryList));

        LinkBuilderFactory.submitLink(saveCategoryGroup())
                .usingDefaults()
                .body(new ResourceModel("save"))
                .attach(form, "save");

        this.add(form);
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxLink<Category>> deleteCategory() {
        return (ajaxRequestTarget, components) -> {
            CategoryGroupPage parent = components.findParent(CategoryGroupPage.class);
            parent.getModelObject().getCategoryList().remove(components.getModelObject());
            ajaxRequestTarget.add(parent);
        };
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxSubmitLink> saveCategoryGroup() {
        return (ajaxRequestTarget, components) -> {
            CategoryGroupPage parent = components.findParent(CategoryGroupPage.class);
            parent.categoryGroupService.save(parent.getModelObject());
            ajaxRequestTarget.add(parent);
        };
    }

    @SuppressWarnings("unchecked")
    private BaseForm<Category> getCategoryForm() {
        return (BaseForm<Category>) this.getForm().get(CATEGORY_FORM_ID);
    }

    @SuppressWarnings("unchecked")
    public BaseForm<Category> getForm() {
        return (BaseForm<Category>) this.get(FORM_ID);
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxSubmitLink> saveCategory() {
        return (ajaxRequestTarget, components) -> {
            CategoryGroupPage parent = components.findParent(CategoryGroupPage.class);
            parent.getModelObject().addCategory(parent.toeTeVoegenCategoryModel.getObject());
            parent.toeTeVoegenCategoryModel.setObject(null);

            ajaxRequestTarget.add(parent);

        };
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxSubmitLink> addCategory() {
        return (ajaxRequestTarget, components) -> {
            CategoryGroupPage parent = components.findParent(CategoryGroupPage.class);
            parent.toeTeVoegenCategoryModel.setObject(new Category());
            parent.getCategoryForm().getFormModeModel().setObject(BaseForm.FormMode.EDIT);
            ajaxRequestTarget.add(parent.getCategoryForm());
        };
    }
}
