package be.ghostwritertje.domain.budgetting;

import be.ghostwritertje.domain.DomainObject;
import be.ghostwritertje.domain.Person;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Table(name = "T_BANKACCOUNT", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"number", "administrator_UUID"}
        )
})
@Entity
public class BankAccount extends DomainObject {

    @ManyToOne(optional = false)
    @JoinColumn(name = "ADMINISTRATOR_UUID")
    private Person administrator;

    @ManyToOne
    @JoinColumn(name = "OWNER_UUID")
    private Person owner;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BANK_UUID")
    private Bank bank;

    @Column(nullable = false)
    private String number;

    @Column
    private String name;

    @Formula(value = "(SELECT SUM(statement.AMOUNT) FROM T_STATEMENT statement WHERE statement.ORIGINATINGACCOUNT_UUID = UUID)")
    @Access(AccessType.FIELD)
    private BigDecimal balance;

    public Person getOwner() {
        return this.owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Person getAdministrator() {
        return this.administrator;
    }

    public void setAdministrator(Person administrator) {
        this.administrator = administrator;
    }

    public Bank getBank() {
        if(this.bank == null){
            this.bank = new Bank();
        }
        return this.bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return this.name != null ? this.name : this.number;
    }
}
