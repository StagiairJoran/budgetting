package be.ghostwritertje.webapp.charts;

import be.ghostwritertje.views.budgetting.CategoryGroupView;
import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.DataLabels;
import com.googlecode.wickedcharts.highcharts.options.PixelOrPercent;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.color.HexColor;
import com.googlecode.wickedcharts.highcharts.options.series.Point;
import com.googlecode.wickedcharts.highcharts.options.series.PointSeries;
import com.googlecode.wickedcharts.highcharts.options.series.Series;
import com.googlecode.wickedcharts.wicket7.highcharts.Chart;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 29-Apr-17.
 */
public class PieChartBuilder extends ChartBuilderSupport<PieChartBuilder> {


    private SerializableFunction<List<Series<?>>, List<Series<?>>> seriesListConsumer = pointSeries1 -> pointSeries1;


    public PieChartBuilder() {
        this.consume(options -> {

            ChartOptions chartOptions = new ChartOptions();
            chartOptions
                    .setType(SeriesType.PIE);
            options.setChartOptions(chartOptions);
        });
    }

    public PieChartBuilder addPoints(IModel<List<CategoryGroupView>> categoryGroups) {
        this.seriesListConsumer = seriesList -> {

            DataLabels dataLabels1 = new DataLabels();
            dataLabels1
                    .setColor(new HexColor("#ffffff"));
            dataLabels1
                    .setDistance(-30);

            PointSeries pointSeries1 = new PointSeries();
            pointSeries1.setType(SeriesType.PIE);
            categoryGroups.getObject().forEach(o -> pointSeries1.addPoint(new Point(o.getDisplayValue(), (Number) o.getNumberDisplayValue())));
            pointSeries1.setSize(new PixelOrPercent(60, PixelOrPercent.Unit.PERCENT));
            pointSeries1.setDataLabels(dataLabels1);
            seriesList.add(pointSeries1);

            PointSeries pointSeries2 = new PointSeries();
            pointSeries2.setType(SeriesType.PIE);
            pointSeries2.setInnerSize(new PixelOrPercent(60, PixelOrPercent.Unit.PERCENT));
            categoryGroups.getObject().forEach(o -> o.getCategoryList().forEach(c -> pointSeries2.addPoint(new Point(c.getDisplayValue(), (Number) c.getNumberDisplayValue()))));
            pointSeries2.setDataLabels(new DataLabels());
            seriesList.add(pointSeries2);

            return seriesList;
        };

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
