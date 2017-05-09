package be.ghostwritertje.views.budgetting;

import be.ghostwritertje.domain.DomainObject;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by Jorandeboever on 5/9/2017.
 */
@Entity
@Immutable
@Table(name = "V_CATEGORY")
public class CategoryView extends DomainObject {
    private static final long serialVersionUID = 114706258813996614L;

    private BigDecimal amount;

    public BigDecimal getAmount() {
        return this.amount;
    }
}
