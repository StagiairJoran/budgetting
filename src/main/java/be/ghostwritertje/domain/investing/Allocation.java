package be.ghostwritertje.domain.investing;

import be.ghostwritertje.domain.DomainObject;
import be.ghostwritertje.utilities.Pair;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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

    private Double allocation;

    public Allocation() {
    }

    public Allocation(FinancialInstrument financialInstrument, Double allocation) {
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

    public Double getAllocation() {
        return allocation;
    }

    public void setAllocation(Double allocation) {
        this.allocation = allocation;
    }

    public List<Pair<LocalDate, Double>> getAllocationAdjustedValuesFromStartDate(LocalDate date) {
        return this.financialInstrument.getHistoricPriceList().stream()
                .filter(historicPrice -> historicPrice.getDate().isBefore(date))
                .sorted(Comparator.comparing(HistoricPrice::getDate).reversed())
                .findFirst()
                .map(historicPrice -> {
                    double value = 10000 / historicPrice.getPrice();
                    return this.financialInstrument.getHistoricPriceList().stream()
                            .filter(h -> h.getDate().isAfter(date))
                            .map(historicPrice1 -> new Pair<>(historicPrice1.getDate(), historicPrice1.getPrice() * value * allocation))
                            .collect(Collectors.toList());
                }).orElse(new ArrayList<>());
    }
}
