package be.ghostwritertje.webapp.charts;

import com.googlecode.wickedcharts.highcharts.options.series.Coordinate;

import java.time.LocalDate;

/**
 * Created by Jorandeboever
 * Date: 19-Dec-16.
 */
public class DateCoordinate extends Coordinate<String, Float> {
    public DateCoordinate(LocalDate date, Double number) {
        super(convertToString(date), number.floatValue());
    }

    private static String convertToString(LocalDate date) {
        return String.format("Date.UTC(%s,  %s,  %s)", date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
    }
}
