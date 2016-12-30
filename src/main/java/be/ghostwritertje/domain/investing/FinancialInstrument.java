package be.ghostwritertje.domain.investing;

import be.ghostwritertje.domain.DomainObject;
import be.ghostwritertje.utilities.CalculatorUtilities;
import be.ghostwritertje.utilities.Pair;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Jorandeboever
 * Date: 10-Oct-16.
 */
@Entity
@Table(name = "T_FINANCIAL_INSTRUMENT")
public class FinancialInstrument extends DomainObject {
    @Column(unique = true)
    private String quote;

    @OneToMany(mappedBy = "financialInstrument", fetch = FetchType.EAGER) //TODO should not be eager
    private final List<HistoricPrice> historicPriceList = new ArrayList<>();

    public FinancialInstrument() {
    }

    public Double getCurrentPrice() {
        return this.historicPriceList.stream()
                .sorted(Comparator.comparing(HistoricPrice::getDate).reversed())
                .findFirst()
                .map(HistoricPrice::getPrice)
                .orElse(null);
    }

    public Double getYearToDateReturn() {
        return this.historicPriceList.stream()
                .filter(historicPrice -> historicPrice.getDate().isBefore(LocalDate.now().minusYears(1)))
                .sorted(Comparator.comparing(HistoricPrice::getDate).reversed())
                //TODO price may not be exactly a year ago
                .findFirst()
                .map(historicPrice -> Optional.ofNullable(this.getCurrentPrice())
//                        .map(currentPrice -> currentPrice / historicPrice.getPrice())
                        .map(currentPrice -> CalculatorUtilities.calculateAnnualizedReturn(BigDecimal.valueOf(historicPrice.getPrice()), BigDecimal.valueOf(currentPrice), 1).doubleValue())
                        .orElse(null))
                .orElse(null);
    }

    public Double get5yearReturn() {
        int yearsToSubtract = 5;
        return this.historicPriceList.stream()
                .filter(historicPrice -> historicPrice.getDate().isBefore(LocalDate.now().minusYears(yearsToSubtract)))
                .sorted(Comparator.comparing(HistoricPrice::getDate).reversed())
                //TODO price may not be exactly a year ago
                .findFirst()
                .map(historicPrice -> Optional.ofNullable(this.getCurrentPrice())
                        .map(currentPrice -> CalculatorUtilities.calculateAnnualizedReturn(BigDecimal.valueOf(historicPrice.getPrice()), BigDecimal.valueOf(currentPrice), yearsToSubtract).doubleValue())
                        .orElse(null))
                .orElse(null);
    }

    public FinancialInstrument(String quote) {
        this.quote = quote;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public List<HistoricPrice> getHistoricPriceList() {
        return historicPriceList;
    }

    public List<Pair<LocalDate, Double>> getValuesFromStartDate(LocalDate date) {
        return this.historicPriceList.stream()
                .filter(historicPrice -> historicPrice.getDate().isBefore(date))
                .sorted(Comparator.comparing(HistoricPrice::getDate).reversed())
                .findFirst()
                .map(historicPrice -> {
                    BigDecimal value = new BigDecimal("10000").divide(BigDecimal.valueOf(historicPrice.getPrice()), RoundingMode.HALF_EVEN);
                    return this.historicPriceList.stream()
                            .filter(h -> h.getDate().isAfter(date))
                            .map(historicPrice1 -> new Pair<>(historicPrice1.getDate(), BigDecimal.valueOf(historicPrice1.getPrice()).multiply(value).doubleValue()))
                            .collect(Collectors.toList());
                }).orElse(new ArrayList<>());
    }
}
