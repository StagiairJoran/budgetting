package be.ghostwritertje.webapp.charts;

import com.googlecode.wickedcharts.highcharts.options.Axis;
import com.googlecode.wickedcharts.highcharts.options.AxisType;
import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.DateTimeLabelFormat;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.Title;
import com.googlecode.wickedcharts.highcharts.options.series.Coordinate;
import com.googlecode.wickedcharts.highcharts.options.series.CustomCoordinatesSeries;
import com.googlecode.wickedcharts.wicket7.highcharts.Chart;
import org.apache.wicket.MarkupContainer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jorandeboever
 * Date: 20-Dec-16.
 */
public class HistoricChartBuilder {
    private final Options options = new Options().setChartOptions(new ChartOptions().setType(SeriesType.SPLINE));

    public HistoricChartBuilder() {
        setXAxis();
        setYAxis();
    }

    private HistoricChartBuilder setXAxis() {
        Axis xAxis = new Axis();
        xAxis
                .setType(AxisType.DATETIME);

        DateTimeLabelFormat dateTimeLabelFormat = new DateTimeLabelFormat()
                .setProperty(DateTimeLabelFormat.DateTimeProperties.MONTH, "%e. %b")
                .setProperty(DateTimeLabelFormat.DateTimeProperties.YEAR, "%b");
        xAxis
                .setDateTimeLabelFormats(dateTimeLabelFormat);
        options.setxAxis(xAxis);
        return this.self();
    }

    private HistoricChartBuilder setYAxis() {
        Axis yAxis = new Axis();
        yAxis
                .setTitle(new Title("Kilometres driven (m)"));
        yAxis
                .setMin(0);
        options
                .setyAxis(yAxis);
        return this.self();
    }

    private HistoricChartBuilder self() {
        return this;
    }

    public HistoricChartBuilder addLine(String name, List<DateCoordinate> coordinates) {
        CustomCoordinatesSeries<String, Float> series1 = new CustomCoordinatesSeries<String, Float>();
        series1
                .setName(name);
        series1
                .setData(coordinates.stream().map(dateCoordinate -> (Coordinate<String, Float>) dateCoordinate).collect(Collectors.toList()));
        options.addSeries(series1);
        return this.self();
    }

    public HistoricChartBuilder usingDefaults() {
        return this.self();
    }

    public HistoricChartBuilder title(String title) {
        options.setTitle(new Title(title));
        return this.self();
    }

    public HistoricChartBuilder subTitle(String title) {
        options.setSubtitle(new Title(title));
        return this.self();
    }

    public HistoricChartBuilder attach(MarkupContainer initialParent, String id) {
        Chart chart = this.build(id);
        initialParent.add(chart);
        return this.self();
    }

    private Chart build(String id) {
        Chart chart = new Chart(id, options);

        return chart;
    }
}
