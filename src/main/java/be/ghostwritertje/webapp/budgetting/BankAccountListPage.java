package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.BankAccount;
import be.ghostwritertje.services.budgetting.BankAccountService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.datatable.ColumnBuilderFactory;
import be.ghostwritertje.webapp.datatable.DataTableBuilderFactory;
import be.ghostwritertje.webapp.investing.FundPurchaseListPage;
import be.ghostwritertje.webapp.model.DomainObjectListModel;
import org.apache.wicket.extensions.markup.html.repeater.data.table.LambdaColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

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

        this.add(DataTableBuilderFactory.<BankAccount, String>simple()
                .addColumn(new LambdaColumn<>(new ResourceModel("number"), BankAccount::getNumber))
                .addColumn(new LambdaColumn<>(new ResourceModel("username"),  b -> b.getAdministrator().getUsername()))
                .addColumn(new LambdaColumn<>(new ResourceModel("total"), BankAccount::getDisplayValue))
                .addColumn(ColumnBuilderFactory.actions(new ResourceModel("actions"), (target, link) -> this.setResponsePage(new StatementListPage(link.getModel())),
                        (target, link) -> {
                            this.bankAccountService.delete(link.getModelObject());
                            this.setResponsePage(new FundPurchaseListPage(BankAccountListPage.this.getModel()));
                        }
                ))
                .build("bankAccounts", new DomainObjectListModel<BankAccount>(this.bankAccountService)));
    }
}
