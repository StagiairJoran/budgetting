package be.ghostwritertje.webapp.charts;

import be.ghostwritertje.views.budgetting.CategoryGroupView;
import be.ghostwritertje.views.budgetting.CategoryView;
import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.DataLabels;
import com.googlecode.wickedcharts.highcharts.options.PixelOrPercent;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.color.HexColor;
import com.googlecode.wickedcharts.highcharts.options.color.HighchartsColor;
import com.googlecode.wickedcharts.highcharts.options.series.Point;
import com.googlecode.wickedcharts.highcharts.options.series.PointSeries;
import com.googlecode.wickedcharts.highcharts.options.series.Series;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 29-Apr-17.
 */
public class PieChartBuilder extends ChartBuilderSupport<PieChartBuilder> {

    PieChartBuilder() {

        ChartOptions chartOptions = new ChartOptions();
        chartOptions
                .setType(SeriesType.PIE);
        this.getOptions().setChartOptions(chartOptions);
    }

    public PieChartBuilder addPoints(IModel<List<CategoryGroupView>> categoryGroups) {
        this.consume(options -> {
            List<Series<?>> seriesList = new ArrayList<>();
            DataLabels dataLabels1 = new DataLabels();
            dataLabels1.setColor(new HexColor("#ffffff"));
            dataLabels1.setDistance(-30);

            PointSeries pointSeries1 = new PointSeries();
            pointSeries1.setType(SeriesType.PIE);
            pointSeries1.setSize(new PixelOrPercent(60, PixelOrPercent.Unit.PERCENT));
            pointSeries1.setDataLabels(dataLabels1);

            PointSeries pointSeries2 = new PointSeries();
            pointSeries2.setType(SeriesType.PIE);
            pointSeries2.setInnerSize(new PixelOrPercent(60, PixelOrPercent.Unit.PERCENT));
            pointSeries2.setDataLabels(new DataLabels());

            int i = 1;
            for (CategoryGroupView categoryGroup : categoryGroups.getObject()) {
                pointSeries1.addPoint(new Point(categoryGroup.getDisplayValue(), categoryGroup.getNumberDisplayValue(), new HighchartsColor(++i)));
                Float brightness = 0.01F;
                for (CategoryView category : categoryGroup.getCategoryList()) {
                    pointSeries2.addPoint(new Point(category.getDisplayValue(), category.getNumberDisplayValue(), new HighchartsColor(i).brighten(brightness)));
                    brightness += 0.05F;
                }
            }
            seriesList.add(pointSeries1);
            seriesList.add(pointSeries2);

            options.setSeries(seriesList);
        });

        return this.self();
    }

}
