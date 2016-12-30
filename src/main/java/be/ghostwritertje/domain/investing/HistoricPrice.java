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
        @UniqueConstraint(name = "un_historic_price_01", columnNames = {"financial_instrument_id", "date"})
})
public class HistoricPrice extends DomainObject {
    @ManyToOne
    @JoinColumn(name = "financial_instrument_id", nullable = false)
    private FinancialInstrument financialInstrument;
    private LocalDate date;

    @Embedded
    private Amount price;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setAmount(Amount price) {
        this.price = price;
    }

    public void getAmount(Amount price) {
        if(this.price == null){
            this.price = new Amount(Currency.Enum.EUR);
        }
        this.price = price;
    }

    public Double getPrice() {
        return this.price.getAmount();
    }

    public void setPrice(Double price) {
        this.price.setAmount(price);
    }

    public FinancialInstrument getFinancialInstrument() {
        return financialInstrument;
    }

    public void setFinancialInstrument(FinancialInstrument financialInstrument) {
        this.financialInstrument = financialInstrument;
    }
}
