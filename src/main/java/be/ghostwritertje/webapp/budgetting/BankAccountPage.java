package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.budgetting.Bank;
import be.ghostwritertje.domain.budgetting.BankAccount;
import be.ghostwritertje.services.budgetting.BankAccountService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.form.BaseForm;
import be.ghostwritertje.webapp.form.FormComponentBuilderFactory;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;

import java.util.Arrays;

/**
 * Created by Jorandeboever
 * Date: 15-Apr-17.
 */
public class BankAccountPage extends BasePage<BankAccount> {
    private static final long serialVersionUID = 7297216776778133376L;

    @SpringBean
    private BankAccountService bankAccountService;

    public BankAccountPage(IModel<BankAccount> model) {
        super(model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        LinkBuilderFactory.pageLink(() -> LambdaModel.of(this.getModel(), BankAccount::getAdministrator), BankAccountListPage::new)
                .usingDefaults()
                .attach(this, "back");

        BaseForm<BankAccount> form = new BaseForm<>("form", this.getModel());

        FormComponentBuilderFactory.textField()
                .usingDefaults()
                .body(new ResourceModel("name"))
                .attach(form, "name", LambdaModel.of(this.getModel(), BankAccount::getName, BankAccount::setName));

        FormComponentBuilderFactory.textField()
                .usingDefaults()
                .body(new ResourceModel("number"))
                .attach(form, "number", LambdaModel.of(this.getModel(), BankAccount::getNumber, BankAccount::setNumber));

        FormComponentBuilderFactory.<Bank>dropDown()
                .usingDefaults()
                .body(new ResourceModel("bank"))
                .attach(form, "bank", LambdaModel.of(this.getModel(), BankAccount::getBank, BankAccount::setBank), () -> Arrays.asList(Bank.KEYTRADE, Bank.BELFIUS));

        LinkBuilderFactory.submitLink(save())
                .usingDefaults()
                .attach(form, "save");

        this.add(form);
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxSubmitLink> save() {
        return (target, components) -> {
            BankAccountPage parent = components.findParent(BankAccountPage.class);
            parent.setModelObject(parent.bankAccountService.save(parent.getModelObject()));
        };

    }
}
