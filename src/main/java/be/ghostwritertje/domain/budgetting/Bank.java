package be.ghostwritertje.domain.budgetting;

import be.ghostwritertje.domain.DomainObject;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Entity
@Table(name = "T_BANK")
public class Bank extends DomainObject {
    private static final long serialVersionUID = -3888386652005214793L;

    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
