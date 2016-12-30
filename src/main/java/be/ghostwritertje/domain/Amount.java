package be.ghostwritertje.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class Amount {
    @Column(name = "AMOUNT")
    private Double value;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "AMOUNT_TYPE_UUID")
    private AmountType amountType;

    public Amount() {
    }

    public <T extends AmountType, E extends AmountTypeEnum<T>> Amount(E amountTypeEnum) {
        this.amountType = new AmountType(amountTypeEnum);
    }

    public boolean isCurrencyType(){
        return this.amountType instanceof Currency;
    }

    public Double getValue() {
        return this.value;
    }

    public void setValue(Double amount) {
        this.value = amount;
    }

    public AmountType getAmountType() {
        return this.amountType;
    }

    public void setAmountType(AmountType amountType) {
        this.amountType = amountType;
    }
}
