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
    }

    private HistoricChartBuilder setXAxis() {
        options.setxAxis(new Axis()
                .setType(AxisType.DATETIME)
                .setDateTimeLabelFormats( new DateTimeLabelFormat()
                .setProperty(DateTimeLabelFormat.DateTimeProperties.MONTH, "%e. %b")
                .setProperty(DateTimeLabelFormat.DateTimeProperties.YEAR, "%b")));
        return this.self();
    }

    public HistoricChartBuilder setYAxis(String title) {
        options.setyAxis(new Axis()
                .setTitle(new Title(title)));
        return this.self();
    }

    private HistoricChartBuilder self() {
        return this;
    }

    public HistoricChartBuilder addLine(String name, List<DateCoordinate> coordinates) {
        options.addSeries( new CustomCoordinatesSeries<String, Float>()
                .setName(name)
                .setData(coordinates.stream()
                        .map(dateCoordinate -> (Coordinate<String, Float>) dateCoordinate)
                        .collect(Collectors.toList())));
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
