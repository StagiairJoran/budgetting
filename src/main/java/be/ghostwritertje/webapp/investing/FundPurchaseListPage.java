package be.ghostwritertje.webapp.investing;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.investing.FundPurchase;
import be.ghostwritertje.repository.configuration.datasource.H2DataSource;
import be.ghostwritertje.services.investing.FinanceService;
import be.ghostwritertje.services.investing.FundPurchaseService;
import be.ghostwritertje.services.investing.InvestmentSummary;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.CustomSession;
import be.ghostwritertje.webapp.IModelBasedVisibilityBehavior;
import com.google.common.util.concurrent.AbstractFuture;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Jorandeboever
 * Date: 08-Oct-16.
 */
public class FundPurchaseListPage extends BasePage<Person> {
    private static final Logger logger = Logger.getLogger(H2DataSource.class);
    @SpringBean
    private FundPurchaseService fundPurchaseService;

    @SpringBean
    private FinanceService financeService;

    private IModel<List<FundPurchase>> fundPurchaseListModel;

    private IModel<InvestmentSummary> investmentSummaryModel;
    private IModel<Double> totalSumModel = new Model<>();

    public FundPurchaseListPage() {
        this(new Model<>(CustomSession.get().getLoggedInPerson()));
    }

    public FundPurchaseListPage(IModel<Person> model) {
        super(model);
        this.investmentSummaryModel = new Model<InvestmentSummary>();
        this.fundPurchaseListModel = new ListModel<>(this.fundPurchaseService.findByOwner(this.getModelObject()));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        this.investmentSummaryModel.setObject(this.financeService.calculateInvestmentSummary(this.fundPurchaseListModel.getObject()));
        this.add(new Label("totalInvested", this.investmentSummaryModel.getObject().getTotalInvested()));
        this.add(new Label("totalCount", this.fundPurchaseListModel.getObject().stream().map(FundPurchase::getNumberOfShares).mapToInt(Number::intValue).sum()));
        this.add(new Label("addedValue", this.investmentSummaryModel.getObject().getAddedValue()));
        this.add(new Label("totalSum", this.investmentSummaryModel.getObject().getCurrentValue()));

        this.add(new Label("annualPerformance", String.format("%.2f", Optional.ofNullable(this.investmentSummaryModel.getObject().getAnnualPerfomanceInPercentage()).map(a -> a.multiply(BigDecimal.valueOf(100))).orElse(null)))
                .add(new IModelBasedVisibilityBehavior<>(this.investmentSummaryModel, investmentSummary -> investmentSummary.getAnnualPerfomanceInPercentage() != null))
                .setOutputMarkupPlaceholderTag(true));

        this.add(new Label("addedValuePercentage", String.format("%.2f", this.investmentSummaryModel.getObject().getAddedValueInPercentage().multiply(BigDecimal.valueOf(100))))
                .add(new IModelBasedVisibilityBehavior<>(this.investmentSummaryModel, investmentSummary -> investmentSummary.getAnnualPerfomanceInPercentage() == null))
                .setOutputMarkupPlaceholderTag(true));

        this.add(new ListView<FundPurchase>("fundPurchases", this.fundPurchaseListModel) {
            @Override
            protected void onInitialize() {
                super.onInitialize();
                this.setViewSize(25);
            }

            @Override
            protected void populateItem(ListItem<FundPurchase> item) {
                item.add(new Label("date", item.getModelObject().getDate()));
                item.add(new Label("quote", item.getModelObject().getQuote()));
                item.add(new Label("count", item.getModelObject().getNumberOfShares()));
                item.add(new Label("sharePrice", item.getModelObject().getSharePrice()));
                item.add(new Label("transactionCost", item.getModelObject().getTransactionCost()));
                item.add(new Link<FundPurchase>("edit", item.getModel()) {
                    @Override
                    public void onClick() {
                        this.setResponsePage(new FundPurchasePage(this.getModel()));
                    }
                });
                item.add(new Link<FundPurchase>("delete", item.getModel()) {
                    @Override
                    public void onClick() {
                        FundPurchaseListPage.this.fundPurchaseService.delete(this.getModelObject());
                        this.setResponsePage(new FundPurchaseListPage(FundPurchaseListPage.this.getModel()));
                    }
                });
            }
        });

        this.add(new Link<FundPurchase>("newPurchaseLink") {
            @Override
            public void onClick() {
                FundPurchase fundPurchase = new FundPurchase();
                fundPurchase.setOwner(FundPurchaseListPage.this.getModelObject());
                fundPurchase.setDate(LocalDate.now());
                this.setResponsePage(new FundPurchasePage(new Model<FundPurchase>(fundPurchase)));
            }
        });
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Future<Double> future = new AbstractFuture<Double>() {
            @Override
            public Double get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
                return 5.3;
            }
        };
        System.out.println("hello");
        System.out.println("bye");
    }
}
