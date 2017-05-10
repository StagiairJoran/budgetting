package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.services.budgetting.CategoryService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.charts.ChartBuilderFactory;
import be.ghostwritertje.webapp.charts.ChartBuilderSupport;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class StatementListPage extends BasePage<Person> {
    private static final long serialVersionUID = 2304687216679435707L;
    public static final String PIE_CHART_ID = "pieChart";

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
                .addPoints(this.statementListContext.getStatementCountListModel())
                .attach(this, PIE_CHART_ID);

        this.add(new StatementListPanel("statementList", this.statementListContext));
    }

    public void resetChartModels() {
        this.getPieChart().getOptionsIModel().setObject(null);
        this.statementListContext.getStatementCountListModel().setObject(null);
    }


    public ChartBuilderSupport.CustomChart getPieChart() {
        return (ChartBuilderSupport.CustomChart) this.get(PIE_CHART_ID);
    }


}
