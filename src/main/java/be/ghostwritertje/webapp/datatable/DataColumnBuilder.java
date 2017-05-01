package be.ghostwritertje.webapp.datatable;

import org.apache.wicket.extensions.markup.html.repeater.data.table.LambdaColumn;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.danekja.java.util.function.serializable.SerializableSupplier;

import java.util.Optional;

/**
 * Created by Jorandeboever
 * Date: 05-Jan-17.
 */
public class DataColumnBuilder<T, S> extends ColumnBuilderSupport<T, S, LambdaColumn<T,S>> {

    private SerializableSupplier<String> cssSupplier;
    private final SerializableFunction<T, ?>  dataFunction;

    public DataColumnBuilder(SerializableFunction<T, ?> dataFunction) {
        this.dataFunction = dataFunction;
    }

    @SuppressWarnings("unchecked")
    private DataColumnBuilder<T, S> self() {
        return (DataColumnBuilder<T, S>) this;
    }

    public DataColumnBuilder<T, S> hideOnMobile() {
        this.cssSupplier = () -> "hidden-xs";
        return this.self();
    }

    @Override
    public LambdaColumn<T, S> build(IModel<String> headerModel) {
        MyColumn<T, S> column = new MyColumn<>(headerModel, this.dataFunction);
        Optional.ofNullable(this.cssSupplier).ifPresent(column::setCssSupplier);
        return column;
    }

    public static class MyColumn<T, S> extends LambdaColumn<T, S> {
        private SerializableSupplier<String> cssSupplier = () -> null;

        MyColumn(IModel<String> displayModel, SerializableFunction<T, ?> function) {
            super(displayModel, function);
        }

        MyColumn<T, S> setCssSupplier(SerializableSupplier<String> cssSupplier) {
            this.cssSupplier = cssSupplier;
            return this;
        }

        @Override
        public String getCssClass() {
            return cssSupplier.get();
        }
    }
}
