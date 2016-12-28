package be.ghostwritertje.domain.investing;

import be.ghostwritertje.domain.DomainObject;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Jorandeboever
 * Date: 28-Dec-16.
 */
@Entity
@Table(name = "T_ALLOCATION")
public class Allocation extends DomainObject{

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "FINANCIAL_INSTRUMENT_ID")
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
}
