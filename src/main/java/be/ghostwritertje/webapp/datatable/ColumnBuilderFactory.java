package be.ghostwritertje.webapp.datatable;

import org.apache.wicket.Component;
import org.apache.wicket.lambda.WicketBiFunction;
import org.apache.wicket.lambda.WicketConsumer;
import org.apache.wicket.model.IModel;

/**
 * Created by Jorandeboever
 * Date: 29-Dec-16.
 */
public class ColumnBuilderFactory {

    public static <T, S> ActiesColumn<T, S> acties(IModel<String> displayModel, WicketConsumer<IModel<T>> editConsumer, WicketConsumer<IModel<T>> deleteConsumer) {
        return new ActiesColumn<>(displayModel, editConsumer, deleteConsumer);
    }

    public static <T, S> CustomColumn<T, S> custom(IModel<String> displayModel, WicketBiFunction<String, IModel<T>, Component> componentSupplier) {
        return new CustomColumn<>(displayModel, componentSupplier);
    }
}
