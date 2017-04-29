package be.ghostwritertje.webapp.charts;

import com.googlecode.wickedcharts.highcharts.options.*;
import com.googlecode.wickedcharts.highcharts.options.series.Coordinate;
import com.googlecode.wickedcharts.highcharts.options.series.CustomCoordinatesSeries;
import org.danekja.java.util.function.serializable.SerializableFunction;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Jorandeboever
 * Date: 20-Dec-16.
 */
public class HistoricChartBuilder extends ChartBuilderSupport<HistoricChartBuilder> {

    HistoricChartBuilder() {
        this.getOptions().setChartOptions(new ChartOptions().setType(SeriesType.SPLINE));
        this.setXAxis();
    }

    private HistoricChartBuilder setXAxis() {
        this.getOptions().setxAxis(new Axis()
                .setType(AxisType.DATETIME)
                .setDateTimeLabelFormats(new DateTimeLabelFormat()
                        .setProperty(DateTimeLabelFormat.DateTimeProperties.MONTH, "%e. %b")
                        .setProperty(DateTimeLabelFormat.DateTimeProperties.YEAR, "%b")));
        return this.self();
    }

    public HistoricChartBuilder setYAxis(String title) {
        this.getOptions().setyAxis(new Axis()
                .setTitle(new Title(title)));
        return this.self();
    }

    public HistoricChartBuilder addLine(String name, Collection<DateCoordinate> coordinates) {
        this.getOptions().addSeries(new CustomCoordinatesSeries<String, String>()
                .setName(name)
                .setData(coordinates.stream()
                        .map(dateCoordinate -> (Coordinate<String, String>) dateCoordinate)
                        .collect(Collectors.toList())));
        return this.self();
    }

    public <X> HistoricChartBuilder addLine(String name, Collection<X> coordinates, SerializableFunction<X, LocalDate> dateFunction, SerializableFunction<X, Number> numberFunction) {
        this.getOptions().addSeries(new CustomCoordinatesSeries<String, String>()
                .setName(name)
                .setData(coordinates.stream()
                        .map(x -> new DateCoordinate(dateFunction.apply(x), numberFunction.apply(x)))
                        .map(dateCoordinate -> (Coordinate<String, String>) dateCoordinate)
                        .collect(Collectors.toList())));
        return this.self();
    }

    public <X> HistoricChartBuilder addLine(
            String name,
            Collection<X> coordinates,
            SerializableFunction<X, LocalDate> dateFunction,
            SerializableFunction<X, Number> numberFunction,
            int rounding,
            Number average
    ) {
        this.addLine(name, coordinates, dateFunction, numberFunction, rounding);

        this.addLine("average", Arrays.asList(
                coordinates.stream().sorted(Comparator.comparing(dateFunction)).findFirst().orElse(null ),
                coordinates.stream().sorted(Comparator.comparing(dateFunction).reversed()).findFirst().orElse(null )
        ), dateFunction, x -> average, rounding);
        return this.self();

    }

    public <X> HistoricChartBuilder addLine(
            String name,
            Collection<X> coordinates,
            SerializableFunction<X, LocalDate> dateFunction,
            SerializableFunction<X, Number> numberFunction,
            int rounding
    ) {
        this.getOptions().addSeries(new CustomCoordinatesSeries<String, String>()
                .setName(name)
                .setData(coordinates.stream()
                        .map(x -> new DateCoordinate(dateFunction.apply(x), numberFunction.apply(x), rounding))
                        .map(dateCoordinate -> (Coordinate<String, String>) dateCoordinate)
                        .collect(Collectors.toList())));
        return this.self();
    }

    public <X> HistoricChartBuilder addLines(
            Map<String, List<X>> coordinatesMap,
            SerializableFunction<X, LocalDate> dateFunction,
            SerializableFunction<X, Number> numberFunction,
            int rounding
    ) {
        coordinatesMap.forEach((s, xes) -> this.addLine(s, xes, dateFunction, numberFunction, rounding));
        return this.self();
    }



}
