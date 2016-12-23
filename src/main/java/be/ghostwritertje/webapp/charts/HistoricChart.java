package be.ghostwritertje.webapp.charts;


import com.googlecode.wickedcharts.highcharts.options.*;
import com.googlecode.wickedcharts.highcharts.options.color.HexColor;
import com.googlecode.wickedcharts.highcharts.options.color.HighchartsColor;
import com.googlecode.wickedcharts.highcharts.options.series.Coordinate;
import com.googlecode.wickedcharts.highcharts.options.series.CustomCoordinatesSeries;
import com.googlecode.wickedcharts.highcharts.options.series.SimpleSeries;
import com.googlecode.wickedcharts.wicket7.highcharts.Chart;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HistoricChart extends Chart {


    public HistoricChart(String id, List<DateCoordinate> seriesData1) {
        super(id, new Options());
        this.setOptions(buildOptions(seriesData1));
    }

    private Options buildOptions(List<DateCoordinate> coordinates) {
        Options options = new Options();

        ChartOptions chartOptions = new ChartOptions();
        chartOptions
                .setType(SeriesType.SPLINE);
        options
                .setChartOptions(chartOptions);

        options
                .setTitle(new Title("Verbruik"));
        options
                .setSubtitle(new Title(
                        "An example of irregular time data in Highcharts JS"));

        Axis xAxis = new Axis();
        xAxis
                .setType(AxisType.DATETIME);

        DateTimeLabelFormat dateTimeLabelFormat = new DateTimeLabelFormat()
                .setProperty(DateTimeLabelFormat.DateTimeProperties.MONTH, "%e. %b")
                .setProperty(DateTimeLabelFormat.DateTimeProperties.YEAR, "%b");
        xAxis
                .setDateTimeLabelFormats(dateTimeLabelFormat);
        options
                .setxAxis(xAxis);

        Axis yAxis = new Axis();
        yAxis
                .setTitle(new Title("Kilometres driven (m)"));
        yAxis
                .setMin(0);
        options
                .setyAxis(yAxis);

        Tooltip tooltip = new Tooltip();
        tooltip
                .setFormatter(new Function(
                        "return '<b>'+ this.series.name +'</b><br/>'+Highcharts.dateFormat('%e. %b', this.x) +': '+ this.y +' m';"));
        options
                .setTooltip(tooltip);


        CustomCoordinatesSeries<String, String> series1 = new CustomCoordinatesSeries<String, String>();
        series1
                .setName("Verbruik");
        series1
                .setData(coordinates.stream().map(dateCoordinate -> (Coordinate<String, String>) dateCoordinate).collect(Collectors.toList()));
        options.addSeries(series1);

        return options;
    }
}
