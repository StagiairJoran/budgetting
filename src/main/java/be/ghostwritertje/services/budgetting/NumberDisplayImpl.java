package be.ghostwritertje.services.budgetting;

import be.ghostwritertje.services.NumberDisplay;

import java.math.BigDecimal;

/**
 * Created by Jorandeboever on 5/10/2017.
 */
public class NumberDisplayImpl implements NumberDisplay {
    private static final long serialVersionUID = -6795555836014995740L;

    private String displayValue;
    private BigDecimal numberDisplayValue;

    public NumberDisplayImpl(String displayValue, BigDecimal numberDisplayValue) {
        this.displayValue = displayValue;
        this.numberDisplayValue = numberDisplayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    public void setNumberDisplayValue(BigDecimal numberDisplayValue) {
        this.numberDisplayValue = numberDisplayValue;
    }

    @Override
    public String getDisplayValue() {
        return this.displayValue;
    }

    @Override
    public BigDecimal getNumberDisplayValue() {
        return this.numberDisplayValue;
    }
}
