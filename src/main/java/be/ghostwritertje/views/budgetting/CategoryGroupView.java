package be.ghostwritertje.views.budgetting;

import be.ghostwritertje.domain.DomainObject;
import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.CategoryType;
import be.ghostwritertje.services.NumberDisplay;
import org.hibernate.annotations.Immutable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jorandeboever on 5/9/2017.
 */
@Entity
@Immutable
@Table(name = "V_CATEGORY_GROUP")
public class CategoryGroupView extends DomainObject implements NumberDisplay {
    private static final long serialVersionUID = -9206395494959912677L;

    private String name;

    private BigDecimal amount;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "CATEGORY_GROUP_UUID")
    private final List<CategoryView> categoryList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ADMINISTRATOR_UUID")
    private Person administrator;

    @Column(nullable = false, name = "CATEGORY_TYPE")
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    public BigDecimal getAmount() {
        if (this.amount == null) {
            this.amount = BigDecimal.ZERO;
        }
        return this.amount;
    }

    public List<CategoryView> getCategoryList() {
        return this.categoryList;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplayValue() {
        return this.getName();
    }

    @Override
    public BigDecimal getNumberDisplayValue() {
        return this.getAmount().abs();
    }

}
