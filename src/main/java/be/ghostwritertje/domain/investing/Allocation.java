package be.ghostwritertje.domain.investing;

import be.ghostwritertje.domain.DomainObject;
import be.ghostwritertje.utilities.Pair;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jorandeboever
 * Date: 28-Dec-16.
 */
@Entity
@Table(name = "T_ALLOCATION")
public class Allocation extends DomainObject{

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "FINANCIAL_INSTRUMENT_UUID")
    private FinancialInstrument financialInstrument;

    private BigDecimal allocation;

    public Allocation() {
    }

    public Allocation(FinancialInstrument financialInstrument, BigDecimal allocation) {
        this.financialInstrument = financialInstrument;
        this.allocation = allocation;
    }

    public FinancialInstrument getFinancialInstrument() {
        if(this.financialInstrument == null){
            this.financialInstrument = new FinancialInstrument();
        }
        return financialInstrument;
    }

    public void setFinancialInstrument(FinancialInstrument financialInstrument) {
        this.financialInstrument = financialInstrument;
    }

    public BigDecimal getAllocation() {
        return allocation;
    }

    public void setAllocation(BigDecimal allocation) {
        this.allocation = allocation;
    }

    public List<Pair<LocalDate, BigDecimal>> getAllocationAdjustedValuesFromStartDate(LocalDate date) {
        return this.financialInstrument.getHistoricPriceList().stream()
                .filter(historicPrice -> historicPrice.getDate().isBefore(date))
                .sorted(Comparator.comparing(HistoricPrice::getDate).reversed())
                .findFirst()
                .map(historicPrice -> {
                    BigDecimal value = new BigDecimal("10000").divide(historicPrice.getPrice(), RoundingMode.HALF_DOWN);
                    return this.financialInstrument.getHistoricPriceList().stream()
                            .filter(h -> h.getDate().isAfter(date))
                            .map(historicPrice1 -> new Pair<>(historicPrice1.getDate(), historicPrice1.getPrice().multiply(value).multiply(allocation)))
                            .collect(Collectors.toList());
                }).orElse(new ArrayList<>());
    }
}
