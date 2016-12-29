package be.ghostwritertje.webapp.datatable;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.lambda.WicketBiFunction;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Created by Jorandeboever
 * Date: 29-Dec-16.
 */
public class CustomColumn<T, S> extends AbstractColumn<T, S> {
    private final WicketBiFunction<String, IModel<T>, Component> componentSupplier;

    public CustomColumn(IModel<String> displayModel, WicketBiFunction<String, IModel<T>, Component> componentSupplier) {
        super(displayModel);
        this.componentSupplier = componentSupplier;
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
        cellItem.add(componentSupplier.apply(componentId, rowModel));
    }
}
