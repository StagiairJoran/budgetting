package be.ghostwritertje.domain.investing;

import be.ghostwritertje.domain.DomainObject;
import be.ghostwritertje.domain.Person;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by Jorandeboever
 * Date: 08-Oct-16.
 */
@Table(name = "T_FUND_PURCHASE")
@Entity
public class FundPurchase extends DomainObject {
    private static final long serialVersionUID = 3252851418490578339L;

    @ManyToOne
    @JoinColumn(name = "QUOTE", referencedColumnName = "QUOTE")
    private FinancialInstrument financialInstrument;

    @Column(nullable = false)
    private Integer numberOfShares;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Double sharePrice;

    @Column
    private Double transactionCost;

    @ManyToOne
    @JoinColumn(name = "person_UUID")
    private Person owner;

    public Strding getQuote() {
        return this.getFinancialInstrument().getQuote();
    }

//    public void setQuote(String quote) {
//        this.quote = quote;
//    }


    public FinancialInstrument getFinancialInstrument() {
        return this.financialInstrument;
    }

    public void setFinancialInstrument(FinancialInstrument financialInstrument) {
        this.financialInstrument = financialInstrument;
    }

    public Person getOwner() {
        return this.owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getTransactionCost() {
        if (this.transactionCost == null) {
            this.transactionCost = 0.00;
        }
        return this.transactionCost;
    }

    public void setTransactionCost(Double transactionCost) {
        this.transactionCost = transactionCost;
    }

    public Integer getNumberOfShares() {
        if (this.numberOfShares == null) {
            this.numberOfShares = 0;
        }
        return this.numberOfShares;
    }

    public void setNumberOfShares(Integer numberOfShares) {
        this.numberOfShares = numberOfShares;
    }

    public Double getSharePrice() {
        if (this.sharePrice == null) {
            this.sharePrice = 0.00;
        }
        return this.sharePrice;
    }

    public Double getTotalCost() {
        return this.sharePrice * this.numberOfShares.doubleValue() + this.getTransactionCost();
    }

    public void setSharePrice(Double sharePrice) {
        this.sharePrice = sharePrice;
    }
}
