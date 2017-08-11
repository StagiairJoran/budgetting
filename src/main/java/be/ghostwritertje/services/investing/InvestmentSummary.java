package be.ghostwritertje.services.investing;

import be.ghostwritertje.domain.investing.FinancialInstrument;
import be.ghostwritertje.domain.investing.FundPurchase;
import org.nevec.rjm.BigDecimalMath;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Jorandeboever
 * Date: 08-Oct-16.
 */
public class InvestmentSummary implements Serializable {
    private static final long serialVersionUID = -1679373409514815423L;
    private BigDecimal currentValue;
    private List<FundPurchase> fundPurchaseList;

    private Map<FinancialInstrument, Integer> sharesPerFinancialInstrument;

    public List<FundPurchase> getFundPurchaseList() {
        return this.fundPurchaseList;
    }

    public Map<FinancialInstrument, Integer> getSharesPerFinancialInstrument() {
        return this.fundPurchaseList.stream()
                .collect(Collectors.groupingBy(FundPurchase::getFinancialInstrument, Collectors.summingInt(FundPurchase::getNumberOfShares)));
    }

    public BigDecimal getAddedValue() {
        return this.currentValue.subtract(this.getTotalInvested());
    }

    public BigDecimal getAnnualPerfomanceInPercentage() {
        LocalDate portfolioDate = this.getDatePortfolio();
        if (portfolioDate != null && portfolioDate.isBefore(LocalDate.now().minusYears(1))) {
            return BigDecimalMath.pow(
                    this.getCurrentValue().divide(this.getTotalInvested(), 100, RoundingMode.HALF_EVEN),
                    new BigDecimal("365.25").divide(BigDecimal.valueOf(LocalDate.now().toEpochDay()).subtract(BigDecimal.valueOf(portfolioDate.toEpochDay())),100, RoundingMode.HALF_EVEN))
                    .subtract(new BigDecimal("1"));
        } else {
            return null;
        }

    }

    public BigDecimal getAddedValueInPercentage() {
        BigDecimal zero;
        if (Objects.equals(this.getTotalInvested(), BigDecimal.ZERO)) {
            zero = BigDecimal.ZERO;
        } else {
            zero = this.getAddedValue().divide(this.getTotalInvested().abs(),100, RoundingMode.HALF_EVEN);
        }
        return zero;
    }

    public BigDecimal getTotalInvested() {
        return this.fundPurchaseList.stream().map(FundPurchase::getTotalCost).map(BigDecimal::valueOf).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getCurrentValue() {
        return this.currentValue;
    }

    private LocalDate getDatePortfolio() {
        //TODO not accounting for sales yet (only purchases)
        if (this.fundPurchaseList.isEmpty()) {
            return null;
        } else {
            return LocalDate.ofEpochDay(this.fundPurchaseList.stream().map(f -> {
                BigDecimal weight = BigDecimal.valueOf(f.getTotalCost()).divide(this.getTotalInvested(), 100, RoundingMode.HALF_EVEN);
                return BigDecimal.valueOf(f.getDate().toEpochDay()).multiply(weight);
            }).reduce(BigDecimal.ZERO, BigDecimal::add).longValue());
        }
    }

    void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    void setFundPurchaseList(List<FundPurchase> fundPurchaseList) {
        this.fundPurchaseList = fundPurchaseList;
    }
}
