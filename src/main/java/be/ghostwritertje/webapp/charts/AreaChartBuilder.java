package be.ghostwritertje.webapp.charts;

import com.googlecode.wickedcharts.highcharts.options.*;
import com.googlecode.wickedcharts.highcharts.options.series.SimpleSeries;
import org.apache.wicket.model.IModel;

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


    public AreaChartBuilder addSeries(IModel<Map<String, Collection<? extends Number>>> mapModel) {
        this.consume(options -> options.setSeries(mapModel.getObject().entrySet().stream().map(entrySet -> new SimpleSeries()
                .setName(entrySet.getKey())
                .setData(entrySet.getValue().stream()
                        .map(o -> (Number) o)
                        .collect(Collectors.toList()))).collect(Collectors.toList())));
        return this.self();
    }

    public AreaChartBuilder setxAxis(IModel<List<String>> data) {
        this.consume(options -> options.setxAxis(
                new Axis()
                        .setCategories(data.getObject())));
        return this.self();
    }
}
