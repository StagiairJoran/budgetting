package be.ghostwritertje.webapp.model;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.Category;
import be.ghostwritertje.services.budgetting.BankAccountService;
import be.ghostwritertje.services.budgetting.CategoryService;
import be.ghostwritertje.services.budgetting.StatementService;
import be.ghostwritertje.webapp.budgetting.BankAccountListPage;
import be.ghostwritertje.webapp.charts.ChartBuilderFactory;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;

/**
 * Created by Jorandeboever
 * Date: 02-Oct-16.
 */
public class BankAccountListInfoPanel extends GenericPanel<Person> {
    @SpringBean
    private StatementService statementService;

    @SpringBean
    private BankAccountService bankAccountService;

    public BankAccountListInfoPanel(String id, IModel<Person> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        this.add(new Link<Person>("budgettingLink") {
            private static final long serialVersionUID = 8047491203097068280L;

            @Override
            public void onClick() {
                this.setResponsePage(new BankAccountListPage(BankAccountListInfoPanel.this.getModel()));
            }
        });
        this.add(new Label("total",  this.statementService.getTotal(this.getModelObject())));

    }
}
