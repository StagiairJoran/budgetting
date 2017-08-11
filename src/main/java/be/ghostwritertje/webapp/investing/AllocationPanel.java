package be.ghostwritertje.webapp.investing;

import be.ghostwritertje.domain.investing.FinancialInstrument;
import be.ghostwritertje.domain.investing.FundPurchase;
import be.ghostwritertje.services.budgetting.NumberDisplayImpl;
import be.ghostwritertje.services.investing.InvestmentSummary;
import be.ghostwritertje.webapp.charts.ChartBuilderFactory;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Jorandeboever on 8/11/2017.
 */
public class AllocationPanel extends Panel {
    private final IModel<InvestmentSummary> investmentSummaryIModel;

    public AllocationPanel(String id, IModel<InvestmentSummary> investmentSummaryIModel) {
        super(id);
        this.investmentSummaryIModel = investmentSummaryIModel;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        ChartBuilderFactory.pieChart()
                .title("Allocation")
                .addPoints(LambdaModel.of(this.investmentSummaryIModel, investmentSummary -> investmentSummary.getSharesPerFinancialInstrument().entrySet()
                        .stream()
                        .map(e -> new NumberDisplayImpl(e.getKey().getDisplayValue(), e.getKey().getCurrentPrice()
                                .multiply(new BigDecimal(e.getValue()))))
                        .collect(Collectors.toList())))
                .attach(this, "pieChart");


        List<FundPurchase> list = this.investmentSummaryIModel.getObject().getFundPurchaseList();

        Map<FinancialInstrument, Integer> map = list.stream().collect(Collectors.groupingBy(FundPurchase::getFinancialInstrument, Collectors.summingInt(FundPurchase::getNumberOfShares)));
    }
}
