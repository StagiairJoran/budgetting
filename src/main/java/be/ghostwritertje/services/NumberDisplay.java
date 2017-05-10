package be.ghostwritertje.services;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Jorandeboever on 5/9/2017.
 */
public interface NumberDisplay extends Serializable {
    String getDisplayValue();

    BigDecimal getNumberDisplayValue();
}
