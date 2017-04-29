package be.ghostwritertje.webapp.charts;

import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.series.Point;
import com.googlecode.wickedcharts.highcharts.options.series.PointSeries;
import com.googlecode.wickedcharts.wicket7.highcharts.Chart;

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


    public <X> PieChartBuilder addPoint(String name, Number number) {
        this.pointSeries
                .addPoint(new Point(name, number));
        return this.self();
    }

    @Override
    protected Chart build(String id) {
        this.getOptions().addSeries(this.pointSeries);
        return super.build(id);
    }
}
