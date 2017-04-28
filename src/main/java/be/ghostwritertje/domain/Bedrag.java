package be.ghostwritertje.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Embeddable
public class Bedrag implements Serializable {
    private static final long serialVersionUID = 5733465899521194348L;

    @Column(name = "AMOUNT")
    private BigDecimal value;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Access(AccessType.PROPERTY)
    private Currency currency;

    public Bedrag() {
    }

    public Bedrag(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getValue() {
        if(this.value == null){
            this.value = BigDecimal.ZERO;
        }
        return this.value;
    }

    public void setValue(BigDecimal amount) {
        this.value = amount;
    }

    public Currency getCurrency() {
        if(this.currency == null){
            this.setCurrency(Currency.EUR);
        }
        return this.currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return String.format("%.2s %s", this.value, this.currency);
    }
}
