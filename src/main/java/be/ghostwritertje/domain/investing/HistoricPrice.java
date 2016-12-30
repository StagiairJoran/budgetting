package be.ghostwritertje.domain.investing;

import be.ghostwritertje.domain.Amount;
import be.ghostwritertje.domain.Currency;
import be.ghostwritertje.domain.DomainObject;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDate;

/**
 * Created by Jorandeboever
 * Date: 14-Oct-16.
 */
@Entity
@Table(name = "T_HISTORIC_PRICE", uniqueConstraints = {
        @UniqueConstraint(name = "un_historic_price_01", columnNames = {"financial_instrument_UUID", "date"})
})
public class HistoricPrice extends DomainObject {
    @ManyToOne
    @JoinColumn(name = "financial_instrument_UUID", nullable = false)
    private FinancialInstrument financialInstrument;
    private LocalDate date;

    @Embedded
    private Amount amount;

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public Amount getAmount() {
        if(this.amount == null){
            this.amount = new Amount(Currency.Enum.EUR);
        }
        return this.amount;
    }

    public Double getPrice() {
        return this.getAmount().getValue();
    }

    public void setPrice(Double price) {
        this.getAmount().setValue(price);
    }

    public FinancialInstrument getFinancialInstrument() {
        return this.financialInstrument;
    }

    public void setFinancialInstrument(FinancialInstrument financialInstrument) {
        this.financialInstrument = financialInstrument;
    }
}
