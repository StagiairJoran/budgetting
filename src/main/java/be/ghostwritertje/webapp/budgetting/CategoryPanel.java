package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.Category;
import be.ghostwritertje.domain.budgetting.Statement;
import be.ghostwritertje.services.budgetting.CategoryService;
import be.ghostwritertje.services.budgetting.StatementService;
import be.ghostwritertje.webapp.IModelBasedVisibilityBehavior;
import be.ghostwritertje.webapp.form.BaseForm;
import be.ghostwritertje.webapp.form.FormComponentBuilderFactory;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;

import java.util.List;
import java.util.Optional;

/**
 * Created by Jorandeboever
 * Date: 30-Apr-17.
 */
public class CategoryPanel extends GenericPanel<Category> {
    private static final long serialVersionUID = -1962843952194427934L;
    public static final String FORM_ID = "form";

    @SpringBean
    private CategoryService categoryService;

    @SpringBean
    private StatementService statementService;

    private final IModel<Person> administratorModel;
    private final IModel<Statement> statementModel;
    private final IModel<List<Category>> categoriesByAdministratorModel;
    private final SerializableBiConsumer<AjaxRequestTarget, AjaxSubmitLink> onSubmitBiConsumer;

    public CategoryPanel(String id, IModel<Statement> model, IModel<List<Category>> categoriesByAdministratorModel, SerializableBiConsumer<AjaxRequestTarget, AjaxSubmitLink> onSubmitBiConsumer) {
        super(id, LambdaModel.of(model, Statement::getCategory, Statement::setCategory));
        this.statementModel = model;
        this.administratorModel = LambdaModel.of(model, statement -> statement.getOriginatingAccount().getAdministrator());
        this.categoriesByAdministratorModel = categoriesByAdministratorModel;
        this.onSubmitBiConsumer = onSubmitBiConsumer;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        this.setOutputMarkupId(true);

        BaseForm<Category> form = new BaseForm<>(FORM_ID, this.getModel());
        form.getFormModeModel().setObject(BaseForm.FormMode.READ);

        FormComponentBuilderFactory.<Category>dropDown()
                .usingDefaults()
                .switchable(false)
                .behave(() -> new IModelBasedVisibilityBehavior<>(form.getFormModeModel(), formMode -> formMode == BaseForm.FormMode.EDIT))
                .attach(form, "category", this.getModel(), this.categoriesByAdministratorModel);

        LinkBuilderFactory.ajaxLink(edit(form.getFormModeModel()))
                .usingDefaults()
                .behave(() -> new IModelBasedVisibilityBehavior<>(form.getFormModeModel(), formMode -> formMode == BaseForm.FormMode.READ))
                .body(LambdaModel.of(this.statementModel, statement -> {
                    return Optional.ofNullable(statement.getCategory())
                            .map(Category::getName)
                            .orElse("None");
                }))
                .attach(form, "edit");

        LinkBuilderFactory.submitLink(save())
                .usingDefaults()
                .behave(() -> new IModelBasedVisibilityBehavior<>(form.getFormModeModel(), formMode -> formMode == BaseForm.FormMode.EDIT))
                .body(new ResourceModel("save"))
                .attach(form, "save");

        this.add(form);
    }

    private BaseForm<Category> getForm() {
        return (BaseForm<Category>) this.get(FORM_ID);
    }


    private static SerializableBiConsumer<AjaxRequestTarget, AjaxSubmitLink> save() {
        return (ajaxRequestTarget, component) -> {
            CategoryPanel parent = component.findParent(CategoryPanel.class);
            parent.statementService.save(parent.statementModel.getObject());
            parent.getForm().getFormModeModel().setObject(BaseForm.FormMode.READ);
            ajaxRequestTarget.add(parent);
        };
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxLink<Object>> edit(IModel<BaseForm.FormMode> formModeIModel) {
        return (ajaxRequestTarget, components) -> {
            formModeIModel.setObject(BaseForm.FormMode.EDIT);
            ajaxRequestTarget.add(components.findParent(CategoryPanel.class));
        };
    }
}
