package be.ghostwritertje.webapp.charts;

import be.ghostwritertje.services.NumberDisplay;
import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.series.Point;
import com.googlecode.wickedcharts.highcharts.options.series.PointSeries;
import com.googlecode.wickedcharts.wicket7.highcharts.Chart;
import org.danekja.java.util.function.serializable.SerializableFunction;

import java.util.Map;

/**
 * Created by Jorandeboever
 * Date: 29-Apr-17.
 */
public class PieChartBuilder extends ChartBuilderSupport<PieChartBuilder> {

    private final PointSeries pointSeries;

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

    public PieChartBuilder addPoints(Map<String, Number> map) {
        map.forEach(this::addPoint);
        return this.self();
    }

    public PieChartBuilder addPoints(Iterable<? extends NumberDisplay> numberDisplays) {
        numberDisplays.forEach(numberDisplay -> this.addPoint(numberDisplay.getDisplayValue(), numberDisplay.getNumberDisplayValue()));
        return this.self();
    }


    public <X, Y> PieChartBuilder addPoints(Map<X, Y> map, SerializableFunction<X, String> nameFunction, SerializableFunction<Y, Number> numberFunction) {
        map.forEach((s, number) -> this.addPoint(nameFunction.apply(s), numberFunction.apply(number)));
        return this.self();
    }

    @Override
    protected Chart build(String id) {
        this.getOptions().addSeries(this.pointSeries);
        return super.build(id);
    }
}
