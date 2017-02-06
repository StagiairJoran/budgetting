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
import be.ghostwritertje.webapp.datatable.ColumnBuilderFactory;
import be.ghostwritertje.webapp.datatable.DataTableBuilderFactory;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

        this.add(new Label(
                "annualPerformance",
                String.format(
                        "%.2f",
                        Optional.ofNullable(this.investmentSummaryModel.getObject().getAnnualPerfomanceInPercentage())
                                .map(a -> a.multiply(BigDecimal.valueOf(100)))
                                .orElse(null)
                )
        )
                .add(new IModelBasedVisibilityBehavior<>(this.investmentSummaryModel, investmentSummary -> investmentSummary.getAnnualPerfomanceInPercentage() != null))
                .setOutputMarkupPlaceholderTag(true));

        this.add(new Label(
                "addedValuePercentage",
                String.format("%.2f", this.investmentSummaryModel.getObject().getAddedValueInPercentage().multiply(BigDecimal.valueOf(100)))
        )
                .add(new IModelBasedVisibilityBehavior<>(this.investmentSummaryModel, investmentSummary -> investmentSummary.getAnnualPerfomanceInPercentage() == null))
                .setOutputMarkupPlaceholderTag(true));

       this.add(DataTableBuilderFactory.<FundPurchase, String>simple()
                .addColumn(ColumnBuilderFactory.<FundPurchase, String>simple(FundPurchase::getDate).build(new ResourceModel("date")))
                .addColumn(ColumnBuilderFactory.<FundPurchase, String>simple(o -> o.getFinancialInstrument().getName()).build(new ResourceModel("name")))
                .addColumn(ColumnBuilderFactory.<FundPurchase, String>simple(FundPurchase::getNumberOfShares).build(new ResourceModel("count")))
                .addColumn(ColumnBuilderFactory.<FundPurchase, String>simple(FundPurchase::getSharePrice).build(new ResourceModel("share.price")))
                .addColumn(ColumnBuilderFactory.<FundPurchase, String>simple(FundPurchase::getTransactionCost).build(new ResourceModel("transaction.cost")))
                .addColumn(ColumnBuilderFactory.actions(new ResourceModel("actions"), (target, link) -> this.setResponsePage(new FundPurchasePage(link.getModel())),
                        (target, link) -> {
                            FundPurchaseListPage.this.fundPurchaseService.delete(link.getModelObject());
                            this.setResponsePage(new FundPurchaseListPage(FundPurchaseListPage.this.getModel()));
                        }
                ))
                .build("dataTable", this.fundPurchaseListModel));

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


}
