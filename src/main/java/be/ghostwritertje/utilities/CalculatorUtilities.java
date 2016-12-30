package be.ghostwritertje.utilities;

import org.nevec.rjm.BigDecimalMath;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Jorandeboever
 * Date: 26-Dec-16.
 */
public class CalculatorUtilities {

    public static BigDecimal calculateAnnualizedReturn(BigDecimal originalPrice, BigDecimal currentPrice, int numberOfYears){
        return BigDecimalMath.pow(currentPrice.divide(originalPrice,100, RoundingMode.HALF_EVEN), BigDecimal.ONE.divide(BigDecimal.valueOf(numberOfYears), RoundingMode.HALF_EVEN))
                .subtract(BigDecimal.ONE)
                .multiply(new BigDecimal("100"));
    }
}
