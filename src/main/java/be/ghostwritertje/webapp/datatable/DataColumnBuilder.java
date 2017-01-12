package be.ghostwritertje.webapp.datatable;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.LambdaColumn;
import org.apache.wicket.lambda.WicketFunction;
import org.apache.wicket.lambda.WicketSupplier;
import org.apache.wicket.model.IModel;

import java.util.Optional;

/**
 * Created by Jorandeboever
 * Date: 05-Jan-17.
 */
public class DataColumnBuilder<T, S> {

    private WicketSupplier<String> cssSupplier;

    @SuppressWarnings("unchecked")
    private IColumn<T, S> self() {
        return (IColumn<T, S>) this;
    }

    public IColumn<T, S> hideOnMobile() {
        this.cssSupplier = () -> "hidden-xs";
        return this.self();
    }

    public IColumn<T, S> build(IModel<String> headerModel, WicketFunction<T, S> function) {
        MyColumn<T, S> tsLambdaColumn = new MyColumn<>(headerModel, function);
        Optional.ofNullable(this.cssSupplier).ifPresent(tsLambdaColumn::setCssSupplier);
        return tsLambdaColumn;
    }

    public static class MyColumn<T, S> extends LambdaColumn<T, S> {
        private WicketSupplier<String> cssSupplier = () -> null;

        public MyColumn(IModel<String> displayModel, WicketFunction<T, ?> function) {
            super(displayModel, function);
        }

        public MyColumn<T, S> setCssSupplier(WicketSupplier<String> cssSupplier) {
            this.cssSupplier = cssSupplier;
            return (MyColumn<T, S>) this;
        }

        @Override
        public String getCssClass() {
            return cssSupplier.get();
        }
    }
}
