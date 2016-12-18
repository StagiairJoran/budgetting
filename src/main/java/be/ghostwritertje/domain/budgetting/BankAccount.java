package be.ghostwritertje.domain.budgetting;

import be.ghostwritertje.domain.DomainObject;
import be.ghostwritertje.domain.Person;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Table(name = "T_BANKACCOUNT", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"number", "administrator_id"}
        )
})
@Entity
public class BankAccount extends DomainObject {

    @ManyToOne(optional = false)
    @JoinColumn(name = "administrator_id")
    private Person administrator;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Person owner;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @Column(nullable = false)
    private String number;

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Person getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Person administrator) {
        this.administrator = administrator;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
