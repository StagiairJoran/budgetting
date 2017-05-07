package be.ghostwritertje.webapp.charts;

import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.Title;
import com.googlecode.wickedcharts.wicket7.highcharts.Chart;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.danekja.java.util.function.serializable.SerializableConsumer;

import java.io.Serializable;

/**
 * Created by Jorandeboever
 * Date: 29-Apr-17.
 */
public abstract class ChartBuilderSupport<X extends ChartBuilderSupport<X>> {

    private SerializableConsumer<Options> optionsConsumer = options1 -> {
    };

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
        return new CustomChart(id, Model.of(this.getOptions()), this.optionsConsumer);
    }

    public void consume(SerializableConsumer<Options> optionsConsumer) {
        this.optionsConsumer = this.optionsConsumer.andThen(optionsConsumer);
    }

    private static final class CustomChart extends Chart implements Serializable {
        private static final long serialVersionUID = -8779295808983399888L;

        private final IModel<Options> optionsIModel;
        private final SerializableConsumer<Options> optionsConsumer;

        public CustomChart(String id, IModel<Options> optionsIModel, SerializableConsumer<Options> optionsConsumer) {
            super(id, optionsIModel.getObject());
            this.optionsIModel = optionsIModel;
            this.optionsConsumer = optionsConsumer;
        }

        @Override
        public Options getOptions() {
            return this.optionsIModel.getObject();
        }

        @Override
        protected void onConfigure() {
            super.onConfigure();
            this.optionsConsumer.accept(this.getOptions());

            super.setOptions(this.optionsIModel.getObject());
        }
    }
}
