package be.ghostwritertje.utilities;

import org.apache.log4j.Logger;
import org.nevec.rjm.BigDecimalMath;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Jorandeboever
 * Date: 26-Dec-16.
 */
public class CalculatorUtilities {
    private static final Logger LOG = Logger.getLogger(CalculatorUtilities.class);

    private CalculatorUtilities() {
    }

    public static BigDecimal calculateAnnualizedReturn(BigDecimal originalPrice, BigDecimal currentPrice, int numberOfYears){
        BigDecimal annualizedReturn = BigDecimalMath.pow(currentPrice.divide(originalPrice,100, RoundingMode.HALF_EVEN), BigDecimal.ONE.divide(BigDecimal.valueOf(numberOfYears),100, RoundingMode.HALF_EVEN))
                .subtract(BigDecimal.ONE)
                .multiply(new BigDecimal("100"));
        LOG.debug(String.format("Annualized return for original price (%f) and current price (%f) over (%d) years is %f", originalPrice, currentPrice, numberOfYears, annualizedReturn.doubleValue()));
        return annualizedReturn;
    }

    public static void main(String[] args) {
        System.out.println(CalculatorUtilities.calculateAnnualizedReturn(new BigDecimal("64.589996"), new BigDecimal("116.709999"), 5).doubleValue());
    }
}
