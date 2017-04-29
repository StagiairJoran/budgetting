package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.BankAccount;
import be.ghostwritertje.services.budgetting.BankAccountService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.datatable.ColumnBuilderFactory;
import be.ghostwritertje.webapp.datatable.DataTableBuilderFactory;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import be.ghostwritertje.webapp.model.DomainObjectListModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.table.LambdaColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;

/**
 * Created by Jorandeboever
 * Date: 15-Apr-17.
 */
public class BankAccountListPage extends BasePage<Person> {

    @SpringBean
    private BankAccountService bankAccountService;

    public BankAccountListPage(IModel<Person> model) {
        super(model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        LinkBuilderFactory.ajaxLink(newBankAccount())
                .usingDefaults()
                .attach(this, "new", this.getModel());

        this.add(DataTableBuilderFactory.<BankAccount, String>simple()
                .addColumn(new LambdaColumn<>(new ResourceModel("name"), BankAccount::getName))
                .addColumn(new LambdaColumn<>(new ResourceModel("username"),  b -> b.getAdministrator().getUsername()))
                .addColumn(new LambdaColumn<>(new ResourceModel("balance"), BankAccount::getBalance))
                .addColumn(ColumnBuilderFactory.actions(new ResourceModel("actions"), (target, link) -> this.setResponsePage(new StatementListPage(link.getModel())),
                        (target, link) -> {
                            this.bankAccountService.delete(link.getModelObject());
                            link.setResponsePage(new BankAccountListPage(BankAccountListPage.this.getModel()));
                        }
                ))
                .build("bankAccounts", new DomainObjectListModel<BankAccount, BankAccountService>(this.bankAccountService,service ->  service.findByOwner(this.getModelObject()))));
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxLink<Person>> newBankAccount() {
        return (target, components) -> {
            Person person = components.getModelObject();

            BankAccount bankAccount = new BankAccount();
            bankAccount.setOwner(person);
            bankAccount.setAdministrator(person);

            components.setResponsePage(new BankAccountPage(new Model<>(bankAccount)));
        };
    }
}
