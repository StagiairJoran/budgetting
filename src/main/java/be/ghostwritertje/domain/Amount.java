package be.ghostwritertje.domain;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class Amount {
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "AMOUNT_TYPE_ID")
    private AmountType amountType;

    public boolean isCurrencyType(){
        return this.amountType instanceof Currency;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public AmountType getAmountType() {
        return this.amountType;
    }

    public void setAmountType(AmountType amountType) {
        this.amountType = amountType;
    }
}
