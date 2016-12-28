package be.ghostwritertje.domain.investing;

import be.ghostwritertje.domain.DomainObject;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "PORTFOLIO_ID")
    private final List<Allocation> allocationList = new ArrayList<>();

    public Portfolio() {
    }

    public List<Allocation> getAllocationList() {
        return allocationList;
    }
}
