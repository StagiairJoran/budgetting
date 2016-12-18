package be.ghostwritertje.services.investing;

import be.ghostwritertje.domain.investing.FundPurchase;
import org.nevec.rjm.BigDecimalMath;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 08-Oct-16.
 */
public class InvestmentSummary implements Serializable {
    private BigDecimal currentValue;
    private List<FundPurchase> fundPurchaseList;

    public List<FundPurchase> getFundPurchaseList() {
        return fundPurchaseList;
    }

    public BigDecimal getAddedValue() {
        return currentValue.subtract(getTotalInvested());
    }

    public BigDecimal getAnnualPerfomanceInPercentage() {
        LocalDate portfolioDate = this.getDatePortfolio();
        if (portfolioDate.isBefore(LocalDate.now().minusYears(1))) {
            return BigDecimalMath.pow(
                    this.getCurrentValue().divide(getTotalInvested(), 100, RoundingMode.HALF_EVEN),
                    new BigDecimal("365.25").divide(BigDecimal.valueOf(LocalDate.now().toEpochDay()).subtract(BigDecimal.valueOf(portfolioDate.toEpochDay())), 100, BigDecimal.ROUND_HALF_EVEN))
                    .subtract(new BigDecimal("1"));
        } else {
            return null;
        }
    }

    public BigDecimal getAddedValueInPercentage() {
        return this.getAddedValue().divide(getTotalInvested().abs(), 100, BigDecimal.ROUND_HALF_EVEN);
    }

    public BigDecimal getTotalInvested() {
        return fundPurchaseList.stream().map(FundPurchase::getTotalCost).map(BigDecimal::valueOf).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    private LocalDate getDatePortfolio() {
        //TODO not accounting for sales yet (only purchases)
        return LocalDate.ofEpochDay(fundPurchaseList.stream().map(f -> {
            BigDecimal weight = BigDecimal.valueOf(f.getTotalCost()).divide(this.getTotalInvested(), 100, RoundingMode.HALF_DOWN);
            return BigDecimal.valueOf(f.getDate().toEpochDay()).multiply(weight);
        }).reduce(BigDecimal.ZERO, BigDecimal::add).longValue());
    }

    void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    void setFundPurchaseList(List<FundPurchase> fundPurchaseList) {
        this.fundPurchaseList = fundPurchaseList;
    }
}
