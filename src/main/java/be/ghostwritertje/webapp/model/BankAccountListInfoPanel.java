package be.ghostwritertje.webapp.model;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.Category;
import be.ghostwritertje.services.budgetting.BankAccountService;
import be.ghostwritertje.services.budgetting.CategoryService;
import be.ghostwritertje.services.budgetting.StatementService;
import be.ghostwritertje.webapp.budgetting.BankAccountListPage;
import be.ghostwritertje.webapp.charts.ChartBuilderFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by Jorandeboever
 * Date: 02-Oct-16.
 */
public class BankAccountListInfoPanel extends GenericPanel<Person> {
    @SpringBean
    private StatementService statementService;

    @SpringBean
    private BankAccountService bankAccountService;

    @SpringBean
    private CategoryService categoryService;

    public BankAccountListInfoPanel(String id, IModel<Person> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        this.add(new Link<Person>("budgettingLink") {
            @Override
            public void onClick() {
                setResponsePage(new BankAccountListPage(BankAccountListInfoPanel.this.getModel()));
            }
        });
        this.add(new Label("total", LambdaModel.of(() -> this.statementService.getTotal(this.getModelObject()), a -> {
        })));

        this.categoryService.findCountByAdministrator(this.getModelObject());


        ChartBuilderFactory.pieChart()
                .title("Categories")
                .name("Statements")
                .addPoints(this.categoryService.findCountByAdministrator(this.getModelObject()), Category::getName, aLong -> aLong)
                .attach(this, "pieChart");

    }
}
