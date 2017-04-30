package be.ghostwritertje.domain.budgetting;

import be.ghostwritertje.webapp.form.Display;

import java.io.Serializable;

/**
 * Created by Jorandeboever
 * Date: 30-Apr-17.
 */
public enum Bank implements Display, Serializable {
    KEYTRADE, BELFIUS;

    @Override
    public String getId() {
        return this.name();
    }

    @Override
    public String getDisplayValue() {
        return this.name();
    }

}