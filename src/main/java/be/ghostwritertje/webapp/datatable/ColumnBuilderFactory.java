package be.ghostwritertje.webapp.datatable;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;
import org.danekja.java.util.function.serializable.SerializableBiFunction;
import org.danekja.java.util.function.serializable.SerializableFunction;

/**
 * Created by Jorandeboever
 * Date: 29-Dec-16.
 */
public class ColumnBuilderFactory {


    public static <T, S> ActiesColumn<T, S> actions(IModel<String> displayModel, SerializableBiConsumer<AjaxRequestTarget, AjaxLink<T>> editConsumer, SerializableBiConsumer<AjaxRequestTarget, AjaxLink<T>> deleteConsumer) {
        return new ActiesColumn<>(displayModel, editConsumer, deleteConsumer);
    }

    public static <T, S> CustomColumn<T, S> custom(IModel<String> displayModel, SerializableBiFunction<String, IModel<T>, Component> componentSupplier) {
        return new CustomColumn<>(displayModel, componentSupplier);
    }

    public static <T, S> DataColumnBuilder<T, S> simple(SerializableFunction<T, ?> dataFunction) {
        return new DataColumnBuilder<T,S>(dataFunction);
    }


    public static <T, S> CheckColumnBuilder<T, S> check(){
        return new CheckColumnBuilder<T,S>();
    }
}
