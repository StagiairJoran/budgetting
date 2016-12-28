package be.ghostwritertje.domain.investing;

import be.ghostwritertje.domain.DomainObject;
import be.ghostwritertje.domain.Person;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 28-Dec-16.
 */
@Entity
@Table(name = "T_PORTFOLIO")
public class Portfolio extends DomainObject{

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "PORTFOLIO_ID")
    private final List<Allocation> allocationList = new ArrayList<>();

    private String name;

    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    private Person person;

    public Portfolio() {
    }

    public List<Allocation> getAllocationList() {
        return allocationList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
