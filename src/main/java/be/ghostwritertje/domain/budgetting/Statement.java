package be.ghostwritertje.domain.budgetting;

import be.ghostwritertje.domain.DomainObject;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Entity
@Table(name = "T_STATEMENT")
public class Statement extends DomainObject {
    private static final long serialVersionUID = -5710605280145439611L;

    @ManyToOne
    @JoinColumn(name = "ORIGINATINGACCOUNT_UUID")
    private BankAccount originatingAccount;
    @ManyToOne
    @JoinColumn(name = "DESTINATIONACCOUNT_UUID")
    private BankAccount destinationAccount;
    private LocalDate date;
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_UUID")
    private Category category;

    private String description;

    @Column(name = "CSV_LINE")
    private String csvLine;

    public Statement() {
    }

    public Statement(String description) {
        this.description = description;
    }

    public BankAccount getOriginatingAccount() {
        return this.originatingAccount;
    }

    public void setOriginatingAccount(BankAccount originatingAccount) {
        this.originatingAccount = originatingAccount;
    }

    public BankAccount getDestinationAccount() {
        return this.destinationAccount;
    }

    public void setDestinationAccount(BankAccount destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        if (this.amount == null) {
            this.amount = BigDecimal.ZERO;
        }
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCsvLine() {
        return this.csvLine;
    }

    public void setCsvLine(String csvLine) {
        this.csvLine = csvLine;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
