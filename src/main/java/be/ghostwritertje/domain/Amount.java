package be.ghostwritertje.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class Amount {
    private Double amount;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "AMOUNT_TYPE_ID")
    private AmountType amountType;

    public Amount() {
    }

    public <T extends AmountType, E extends AmountTypeEnum<T>> Amount(E amountTypeEnum) {
        this.amountType = new AmountType(amountTypeEnum);
    }

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
