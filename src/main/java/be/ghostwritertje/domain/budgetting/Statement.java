package be.ghostwritertje.domain.budgetting;

import be.ghostwritertje.domain.DomainObject;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Entity
@Table(name = "T_STATEMENT")
public class Statement extends DomainObject {

    @ManyToOne
    @JoinColumn(name = "ORIGINATINGACCOUNT_UUID")
    private BankAccount originatingAccount;
    @ManyToOne
    @JoinColumn(name = "DESTINATIONACCOUNT_UUID")
    private BankAccount destinationAccount;
    private LocalDate date;
    private BigDecimal amount;

    private String description;

    private String csv_line;

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

    public String getCsv_line() {
        return this.csv_line;
    }

    public void setCsv_line(String csv_line) {
        this.csv_line = csv_line;
    }


}
