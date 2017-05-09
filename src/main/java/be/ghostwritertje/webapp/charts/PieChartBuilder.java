package be.ghostwritertje.webapp.charts;

import be.ghostwritertje.views.budgetting.CategoryGroupView;
import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.series.Point;
import com.googlecode.wickedcharts.highcharts.options.series.PointSeries;
import com.googlecode.wickedcharts.highcharts.options.series.Series;
import com.googlecode.wickedcharts.wicket7.highcharts.Chart;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jorandeboever
 * Date: 29-Apr-17.
 */
public class PieChartBuilder extends ChartBuilderSupport<PieChartBuilder> {

    private final PointSeries pointSeries;

    private SerializableFunction<List<Series<?>>, List<Series<?>>> seriesListConsumer = pointSeries1 -> pointSeries1;

    public PieChartBuilder() {
        this.getOptions().setChartOptions(
                new ChartOptions().setType(SeriesType.PIE)
        );

        this.pointSeries = new PointSeries();
        this.pointSeries.setType(SeriesType.PIE);
    }

    public PieChartBuilder name(String name) {
        this.pointSeries.setName(name);
        return this.self();
    }

    public PieChartBuilder addPoint(String name, Number number) {
        this.pointSeries
                .addPoint(new Point(name, number));
        return this.self();
    }


    public PieChartBuilder addPoints(IModel<List<CategoryGroupView>> numberDisplays) {
        this.seriesListConsumer = seriesList -> {
            PointSeries pointSeries2 = new PointSeries();
            pointSeries2.setType(SeriesType.PIE);
            numberDisplays.getObject().forEach(o -> pointSeries2.addPoint(new Point(o.getDisplayValue(), (Number) o.getNumberDisplayValue())));
            seriesList.add(pointSeries2);
            return seriesList;
        };

        return this.self();
    }

    public <X, Y> PieChartBuilder addPoints(Map<X, Y> map, SerializableFunction<X, String> nameFunction, SerializableFunction<Y, Number> numberFunction) {
        map.forEach((s, number) -> this.addPoint(nameFunction.apply(s), numberFunction.apply(number)));
        return this.self();
    }

    @Override
    protected Chart build(String id) {
//        this.getOptions().addSeries(this.pointSeries);
        this.consume(options -> {
            options.setSeries(this.seriesListConsumer.apply(new ArrayList<>()));
        });
        return super.build(id);
    }
}
