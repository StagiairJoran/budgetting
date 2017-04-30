package be.ghostwritertje.domain.budgetting;

import be.ghostwritertje.domain.DomainObject;
import be.ghostwritertje.domain.Person;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Jorandeboever
 * Date: 30-Apr-17.
 */
@Entity
@Table(name = "T_CATEGORY_GROUP")
public class CategoryGroup extends DomainObject {
    private static final long serialVersionUID = 6772160125295115979L;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ADMINISTRATOR_UUID")
    private Person administrator;

    private String name;

    public Person getAdministrator() {
        return this.administrator;
    }

    public void setAdministrator(Person administrator) {
        this.administrator = administrator;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDisplayValue() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
