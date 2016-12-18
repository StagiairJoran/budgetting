package be.ghostwritertje.services.investing;

import be.ghostwritertje.domain.investing.FinancialInstrument;
import be.ghostwritertje.domain.investing.FundPurchase;
import be.ghostwritertje.domain.investing.HistoricPrice;
import be.ghostwritertje.utilities.DateUtilities;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Jorandeboever
 * Date: 08-Oct-16.
 */
@Service
public class FinanceServiceImpl implements FinanceService {
    private static final Logger logger = Logger.getLogger(FinanceServiceImpl.class);

    @Override
    public Double getTotalPortfolio(List<FundPurchase> fundPurchases) {
        return fundPurchases.stream().map(this::getCurrentTotalValue).mapToDouble(Number::doubleValue).sum();
    }

    public BigDecimal getPriceAtDate(FundPurchase fundPurchase) {
        LocalDate date = fundPurchase.getDate();
        if (date.isEqual(LocalDate.now())) {
            return this.getCurrentValue(fundPurchase);
        } else {
            Calendar calendar = new GregorianCalendar(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            try {
                Stock stock = YahooFinance.get("SWDA.MI", calendar, calendar, Interval.DAILY);
                return Optional.ofNullable(stock.getHistory().get(0)).map(HistoricalQuote::getClose).orElse(BigDecimal.ZERO);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return BigDecimal.ZERO;

    }

    @Override
    public InvestmentSummary calculateInvestmentSummary(List<FundPurchase> fundPurchaseList) {
        InvestmentSummary summary = new InvestmentSummary();
        summary.setCurrentValue(BigDecimal.valueOf(getTotalPortfolio(fundPurchaseList)));
        summary.setFundPurchaseList(fundPurchaseList);
        return summary;
    }

    public BigDecimal getCurrentTotalValue(FundPurchase fundPurchase) {
        return this.getCurrentValue(fundPurchase).multiply(BigDecimal.valueOf(fundPurchase.getNumberOfShares()));
    }

    public BigDecimal getCurrentValue(FundPurchase fundPurchase) {
        try {
            if (fundPurchase.getQuote().isEmpty()) {
                return BigDecimal.ZERO;
            }
            return Optional.ofNullable(YahooFinance.get(fundPurchase.getQuote())).map(stock -> stock.getQuote().getPrice()).orElse(BigDecimal.ZERO);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("STOCKPRICE NOT FOUND! IOEXCEPTION");
            return BigDecimal.ZERO;
        }
    }

    public boolean exists(String quote){
        boolean exists = false;
        try {
            Stock stock = YahooFinance.get(quote);
            exists = !StringUtils.isEmpty(stock.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public List<HistoricPrice> createHistoricPrices(FinancialInstrument financialInstrument) {
        Calendar from = new GregorianCalendar(1950, 1, 1);
        return this.createHistoricPrices(financialInstrument, LocalDate.of(1950, 1,1));
    }

    public List<HistoricPrice> createHistoricPrices(FinancialInstrument financialInstrument, LocalDate date) {
        Calendar from = new GregorianCalendar(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        LocalDate today = LocalDate.now();
        Calendar to = new GregorianCalendar(today.getYear(), today.getMonthValue(), today.getDayOfMonth());

        try {
            return YahooFinance.get(financialInstrument.getQuote(), from, to, Interval.DAILY).getHistory().stream().map(historicalQuote -> convertToHistoricPrice(historicalQuote, financialInstrument)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private HistoricPrice convertToHistoricPrice(HistoricalQuote historicalQuote, FinancialInstrument financialInstrument){
        HistoricPrice historicPrice = new HistoricPrice();
        historicPrice.setDate(DateUtilities.toLocalDate(historicalQuote.getDate().getTime()));
        historicPrice.setPrice(historicalQuote.getClose().doubleValue());
        historicPrice.setFinancialInstrument(financialInstrument);
        return historicPrice;
    }

}
