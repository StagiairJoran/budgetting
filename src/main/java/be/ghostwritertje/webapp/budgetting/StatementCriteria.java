package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.budgetting.BankAccount;
import be.ghostwritertje.domain.budgetting.Category;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Jorandeboever on 5/6/2017.
 */
public class StatementCriteria implements Serializable {

    private BankAccount originatingAccount;
    private String description;
    private Category category;
    private BigDecimal minimum;
    private BigDecimal maximum;

    public BankAccount getOriginatingAccount() {
        return this.originatingAccount;
    }

    public void setOriginatingAccount(BankAccount originatingAccount) {
        this.originatingAccount = originatingAccount;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getMinimum() {
        return this.minimum;
    }

    public void setMinimum(BigDecimal minimum) {
        this.minimum = minimum;
    }

    public BigDecimal getMaximum() {
        return this.maximum;
    }

    public void setMaximum(BigDecimal maximum) {
        this.maximum = maximum;
    }
}
