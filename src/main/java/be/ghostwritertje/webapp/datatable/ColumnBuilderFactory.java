package be.ghostwritertje.webapp.datatable;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.lambda.WicketBiConsumer;
import org.apache.wicket.lambda.WicketBiFunction;
import org.apache.wicket.lambda.WicketFunction;
import org.apache.wicket.model.IModel;

/**
 * Created by Jorandeboever
 * Date: 29-Dec-16.
 */
public class ColumnBuilderFactory {

    public static <T, S> ActiesColumn<T, S> actions(IModel<String> displayModel, WicketBiConsumer<AjaxRequestTarget, AjaxLink<T>> editConsumer, WicketBiConsumer<AjaxRequestTarget, AjaxLink<T>> deleteConsumer) {
        return new ActiesColumn<>(displayModel, editConsumer, deleteConsumer);
    }

    public static <T, S> CustomColumn<T, S> custom(IModel<String> displayModel, WicketBiFunction<String, IModel<T>, Component> componentSupplier) {
        return new CustomColumn<>(displayModel, componentSupplier);
    }

    public static <T, S> DataColumnBuilder<T, S> simple(WicketFunction<T, ?> dataFunction) {
        return new DataColumnBuilder<T,S>(dataFunction);
    }

}
