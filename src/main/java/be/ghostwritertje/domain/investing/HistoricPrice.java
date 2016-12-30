package be.ghostwritertje.domain.investing;

import be.ghostwritertje.domain.Bedrag;
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
    private static final long serialVersionUID = -239280770697690352L;

    @ManyToOne
    @JoinColumn(name = "financial_instrument_UUID", nullable = false)
    private FinancialInstrument financialInstrument;
    private LocalDate date;

    @Embedded
    private Bedrag bedrag;

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setBedrag(Bedrag bedrag) {
        this.bedrag = bedrag;
    }

    public Bedrag getBedrag() {
        if(this.bedrag == null){
            this.bedrag = new Bedrag(Currency.EUR);
        }
        return this.bedrag;
    }

    public Double getPrice() {
        return this.getBedrag().getValue();
    }

    public void setPrice(Double price) {
        this.getBedrag().setValue(price);
    }

    public FinancialInstrument getFinancialInstrument() {
        return this.financialInstrument;
    }

    public void setFinancialInstrument(FinancialInstrument financialInstrument) {
        this.financialInstrument = financialInstrument;
    }
}
