package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.BankAccount;
import be.ghostwritertje.domain.budgetting.Category;
import be.ghostwritertje.services.budgetting.BankAccountService;
import be.ghostwritertje.services.budgetting.CategoryService;
import be.ghostwritertje.webapp.form.BaseForm;
import be.ghostwritertje.webapp.form.FormComponentBuilderFactory;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import be.ghostwritertje.webapp.model.DomainObjectListModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;

import java.util.List;

/**
 * Created by Jorandeboever on 5/6/2017.
 */
public class StatementCriteriaPanel extends GenericPanel<StatementCriteria> {

    public static final String FORM_ID = "form";
    private final IModel<List<BankAccount>> bankAccountsByPersonModel;
    private final IModel<List<Category>> categoryListModel;
    private final SerializableBiConsumer<AjaxRequestTarget, AjaxSubmitLink> onSubmitBiConsumer;

    @SpringBean
    private BankAccountService bankAccountService;
    @SpringBean
    private CategoryService categoryService;

    public StatementCriteriaPanel(String id, IModel<StatementCriteria> model, IModel<Person> personIModel, SerializableBiConsumer<AjaxRequestTarget, AjaxSubmitLink> onSubmitBiConsumer) {
        super(id, model);
        this.bankAccountsByPersonModel = new DomainObjectListModel<BankAccount, BankAccountService>(
                this.bankAccountService,
                service -> service.findByOwner(personIModel.getObject())
        );
        this.categoryListModel = new DomainObjectListModel<Category, CategoryService>(
                this.categoryService,
                service -> service.findByAdministrator(personIModel.getObject())
        );
        this.onSubmitBiConsumer = onSubmitBiConsumer;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        BaseForm<StatementCriteria> form = new BaseForm<>(FORM_ID, this.getModel());

        FormComponentBuilderFactory.textField()
                .usingDefaults()
                .body(new ResourceModel("description"))
                .attach(form, "description", LambdaModel.of(this.getModel(), StatementCriteria::getDescription, StatementCriteria::setDescription));

        FormComponentBuilderFactory.<Category>dropDown()
                .usingDefaults()
                .body(new ResourceModel("category"))
                .attach(form, "category", LambdaModel.of(this.getModel(), StatementCriteria::getCategory, StatementCriteria::setCategory), this.categoryListModel);

        FormComponentBuilderFactory.<BankAccount>dropDown()
                .usingDefaults()
                .body(new ResourceModel("bank"))
                .attach(form, "bankType", LambdaModel.of(this.getModel(), StatementCriteria::getOriginatingAccount, StatementCriteria::setOriginatingAccount), this.bankAccountsByPersonModel);

        LinkBuilderFactory.submitLink(setFormModeEdit().andThen(this.onSubmitBiConsumer))
                .usingDefaults()
                .body(new ResourceModel("search"))
                .attach(form, "search");

        this.add(form);
    }

    @SuppressWarnings("unchecked")
    public BaseForm<StatementCriteria> getForm(){
        return (BaseForm<StatementCriteria>) this.get(FORM_ID);
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxSubmitLink> setFormModeEdit() {
        return (ajaxRequestTarget, components) -> {
            StatementCriteriaPanel parent = components.findParent(StatementCriteriaPanel.class);
            parent.getForm().getFormModeModel().setObject(BaseForm.FormMode.EDIT);
        };
    }


}
