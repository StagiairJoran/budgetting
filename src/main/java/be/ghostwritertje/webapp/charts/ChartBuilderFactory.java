package be.ghostwritertje.webapp.charts;

/**
 * Created by Jorandeboever
 * Date: 20-Dec-16.
 */
public class ChartBuilderFactory {
    private ChartBuilderFactory() {
    }

    public static HistoricChartBuilder splineChart(){
        return new HistoricChartBuilder();
    }
}
