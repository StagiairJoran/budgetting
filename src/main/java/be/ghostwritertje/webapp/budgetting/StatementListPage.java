package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import org.apache.wicket.model.IModel;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class StatementListPage extends BasePage<Person> {
    private static final long serialVersionUID = 2304687216679435707L;

    private final StatementListContext statementListContext;

    StatementListPage(IModel<Person> model) {
        super(model);
        this.statementListContext = new StatementListContext(model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        this.setOutputMarkupId(true);

        LinkBuilderFactory.pageLink(this::getModel, BankAccountListPage::new)
                .usingDefaults()
                .attach(this, "back");

        this.add(new UploadStatementsPanel("upload", this.statementListContext));

        this.add(new StatementListPanel("statementList", this.statementListContext));
    }





}
