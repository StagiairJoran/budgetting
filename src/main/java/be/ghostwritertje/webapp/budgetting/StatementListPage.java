package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.Category;
import be.ghostwritertje.services.budgetting.CategoryService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.charts.ChartBuilderFactory;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class StatementListPage extends BasePage<Person> {
    private static final long serialVersionUID = 2304687216679435707L;

    @SpringBean
    private CategoryService categoryService;

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

        ChartBuilderFactory.pieChart()
                .title("Categories")
                .name("Statements")
                .addPoints(this.categoryService.findCountByAdministrator(this.getModelObject()), Category::getName, aLong -> aLong)
                .attach(this, "pieChart");


        this.add(new StatementListPanel("statementList", this.statementListContext));
    }


}
