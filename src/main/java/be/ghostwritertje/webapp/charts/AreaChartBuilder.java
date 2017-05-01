package be.ghostwritertje.webapp.charts;

import com.googlecode.wickedcharts.highcharts.options.*;
import com.googlecode.wickedcharts.highcharts.options.series.SimpleSeries;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Jorandeboever
 * Date: 01-May-17.
 */
public class AreaChartBuilder extends ChartBuilderSupport<AreaChartBuilder> {

    public AreaChartBuilder() {
        this.getOptions().setChartOptions(new ChartOptions().setType(SeriesType.AREA));

        this.getOptions().setPlotOptions(
                new PlotOptionsChoice()
                        .setArea(new PlotOptions()
                                .setStacking(Stacking.NORMAL))

        );
    }

    public AreaChartBuilder addSeries(String name, Collection<? extends Number> numbers){
        this.getOptions().addSeries(
                new SimpleSeries()
                .setName(name)
                .setData(numbers.stream()
                        .map(o -> (Number) o)
                        .collect(Collectors.toList()))
        );
        return this.self();
    }

    public AreaChartBuilder addSeries(Map<String, Collection<? extends Number>> map){
        map.forEach(this::addSeries);
        return this.self();
    }

    public AreaChartBuilder setxAxis(List<String> data){
        this.getOptions().setxAxis(
                new Axis()
                .setCategories(data)
        );
        return this.self();
    }
}
