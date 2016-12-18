package be.ghostwritertje.services.investing;

import be.ghostwritertje.domain.investing.FinancialInstrument;
import be.ghostwritertje.domain.investing.FundPurchase;
import be.ghostwritertje.domain.investing.HistoricPrice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 08-Oct-16.
 */
public interface FinanceService {
    Double getTotalPortfolio(List<FundPurchase> fundPurchases);

    BigDecimal getCurrentValue(FundPurchase fundPurchase);

    BigDecimal getPriceAtDate(FundPurchase fundPurchase);

    InvestmentSummary calculateInvestmentSummary(List<FundPurchase> fundPurchaseList);

    boolean exists(String quote);

    List<HistoricPrice> createHistoricPrices(FinancialInstrument financialInstrument);

    List<HistoricPrice> createHistoricPrices(FinancialInstrument financialInstrument, LocalDate localDate);
}
