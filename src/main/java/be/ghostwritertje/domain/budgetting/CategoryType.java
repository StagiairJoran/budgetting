package be.ghostwritertje.domain.budgetting;

import be.ghostwritertje.webapp.form.Display;

/**
 * Created by Jorandeboever on 5/7/2017.
 */
public enum CategoryType implements Display {
    INCOME, EXPENSE, UNTRACKED;

    @Override
    public String getId() {
        return this.name();
    }

    @Override
    public String getDisplayValue() {
        return this.name();
    }
}
