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
@Table(name = "V_CATEGORY_GROUP")
public class CategoryGroupView extends DomainObject {
    private static final long serialVersionUID = -9206395494959912677L;

    private BigDecimal amount;

    public BigDecimal getAmount() {
        return this.amount;
    }
}
