package be.ghostwritertje.webapp.charts;

import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.Title;
import com.googlecode.wickedcharts.wicket7.highcharts.Chart;
import org.apache.wicket.MarkupContainer;

/**
 * Created by Jorandeboever
 * Date: 29-Apr-17.
 */
public abstract class ChartBuilderSupport<X extends ChartBuilderSupport<X>> {

    private final Options options = new Options();

    protected Options getOptions() {
        return this.options;
    }

    @SuppressWarnings("unchecked")
    protected X self() {
        return (X) this;
    }

    public X title(String title) {
        this.getOptions().setTitle(new Title(title));
        return this.self();
    }

    public X subTitle(String title) {
        this.getOptions().setSubtitle(new Title(title));
        return this.self();
    }


    public X attach(MarkupContainer initialParent, String id) {
        Chart chart = this.build(id);
        initialParent.add(chart);
        return this.self();
    }

    protected Chart build(String id) {
        return new Chart(id, this.getOptions());
    }

}
