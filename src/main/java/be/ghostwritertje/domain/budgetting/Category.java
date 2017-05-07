package be.ghostwritertje.domain.budgetting;

import be.ghostwritertje.domain.DomainObject;

import javax.persistence.*;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Entity
@Table(name = "T_CATEGORY")
public class Category extends DomainObject {
    private static final long serialVersionUID = -163498981511624588L;


    @ManyToOne(optional = false)
    @JoinColumn(name = "CATEGORY_GROUP_UUID")
    private CategoryGroup categoryGroup;

    private String name;

    public Category() {
    }

    public CategoryGroup getCategoryGroup() {
        return this.categoryGroup;
    }

    public void setCategoryGroup(CategoryGroup categoryGroup) {
        this.categoryGroup = categoryGroup;
    }

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDisplayValue() {
        return this.toString();
    }

    @Override
    public String toString() {
        return String.format("%s - %s", this.categoryGroup.getName(), this.name);
    }
}
