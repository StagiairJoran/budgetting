package be.ghostwritertje.webapp.datatable;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;

/**
 * Created by Jorandeboever
 * Date: 12-Jan-17.
 */
public abstract class ColumnBuilderSupport<T, S, C extends IColumn<T, ?>> {
    public abstract C build(IModel<String> headerModel);
}
