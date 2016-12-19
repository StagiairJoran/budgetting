package be.ghostwritertje.utilities;

/**
 * Created by Jorandeboever
 * Date: 19-Dec-16.
 */
public class NumberUtilities {

    public static Double round(double number, int decimals) {
        double a = Math.pow(10, decimals);
        return Math.round(number * a) / a;
    }
}
