package be.ghostwritertje.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Embeddable
public class Bedrag implements Serializable {
    private static final long serialVersionUID = 5733465899521194348L;

    @Column(name = "AMOUNT")
    private Double value;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Access(AccessType.PROPERTY)
    private Currency currency;

    public Bedrag() {
    }

    public Bedrag(Currency currency) {
        this.currency = currency;
    }

    public Double getValue() {
        if(this.value == null){
            this.value = 0.0;
        }
        return this.value;
    }

    public void setValue(Double amount) {
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
}
