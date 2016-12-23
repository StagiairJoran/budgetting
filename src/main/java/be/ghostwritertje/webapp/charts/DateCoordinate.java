package be.ghostwritertje.webapp.charts;

import com.googlecode.wickedcharts.highcharts.options.series.Coordinate;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;

/**
 * Created by Jorandeboever
 * Date: 19-Dec-16.
 */
public class DateCoordinate extends Coordinate<String, String> {
    public DateCoordinate(LocalDate date, Number number) {
        super(convertToString(date), String.format("%s", number));
    }

    public DateCoordinate(LocalDate date, Number number, int round) {
        super(convertToString(date), new BigDecimal(String.format("%s", number)).setScale(round, BigDecimal.ROUND_HALF_UP).toString());
    }

    private static String convertToString(LocalDate date) {
        return String.format("Date.UTC(%s,  %s,  %s)", date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
    }
}
