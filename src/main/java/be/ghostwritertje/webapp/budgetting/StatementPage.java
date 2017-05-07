package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.BankAccount;
import be.ghostwritertje.domain.budgetting.Category;
import be.ghostwritertje.domain.budgetting.Statement;
import be.ghostwritertje.services.budgetting.BankAccountService;
import be.ghostwritertje.services.budgetting.CategoryService;
import be.ghostwritertje.services.budgetting.StatementService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.form.BaseForm;
import be.ghostwritertje.webapp.form.FormComponentBuilderFactory;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import be.ghostwritertje.webapp.model.DomainObjectListModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Jorandeboever on 5/7/2017.
 */
public class StatementPage extends BasePage<Statement> {

    private final IModel<List<Category>> categoryListModel;
    private final IModel<List<BankAccount>> bankAccountsByPersonModel;

    @SpringBean
    private BankAccountService bankAccountService;
    @SpringBean
    private CategoryService categoryService;
    @SpringBean
    private StatementService statementService;

    public StatementPage(IModel<Statement> model, IModel<Person> personIModel) {
        super(model);

        this.bankAccountsByPersonModel = new DomainObjectListModel<BankAccount, BankAccountService>(
                this.bankAccountService,
                service -> service.findByOwner(personIModel.getObject())
        );
        this.categoryListModel = new DomainObjectListModel<Category, CategoryService>(
                this.categoryService,
                service -> service.findByAdministrator(personIModel.getObject())
        );
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        BaseForm<Statement> form = new BaseForm<>("form", this.getModel());

        FormComponentBuilderFactory.date()
                .usingDefaults()
                .body(new ResourceModel("date"))
                .attach(form, "date", LambdaModel.of(this.getModel(), Statement::getDate, Statement::setDate));

        FormComponentBuilderFactory.textField()
                .usingDefaults()
                .body(new ResourceModel("description"))
                .attach(form, "description", LambdaModel.of(this.getModel(), Statement::getDescription, Statement::setDescription));

        IModel<BigDecimal> bigDecimalIModel = LambdaModel.of(this.getModel(), Statement::getAmount, Statement::setAmount);
        FormComponentBuilderFactory.number(BigDecimal.class)
                .usingDefaults()
                .body(new ResourceModel("amount"))
                .attach(form, "amount", bigDecimalIModel);

        FormComponentBuilderFactory.<BankAccount>dropDown()
                .usingDefaults()
                .body(new ResourceModel("from"))
                .attach(form, "from", LambdaModel.of(this.getModel(), Statement::getOriginatingAccount, Statement::setOriginatingAccount), this.bankAccountsByPersonModel)
                .body(new ResourceModel("to"))
                .attach(form, "to", LambdaModel.of(this.getModel(), Statement::getDestinationAccount, Statement::setDestinationAccount), this.bankAccountsByPersonModel);

        FormComponentBuilderFactory.<Category>dropDown()
                .usingDefaults()
                .body(new ResourceModel("category"))
                .attach(form, "category", LambdaModel.of(this.getModel(), Statement::getCategory, Statement::setCategory), this.categoryListModel);

        LinkBuilderFactory.submitLink(save())
                .usingDefaults()
                .body(new ResourceModel("save"))
                .attach(form,"save" );

        this.add(form);

    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxSubmitLink> save() {
        return (ajaxRequestTarget, components) -> {
            StatementPage parent = components.findParent(StatementPage.class);
            parent.statementService.save(parent.getModelObject());
            ajaxRequestTarget.add(parent);
        };
    }
}
