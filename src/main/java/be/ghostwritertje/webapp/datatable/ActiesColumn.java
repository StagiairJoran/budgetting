package be.ghostwritertje.webapp.datatable;

import org.apache.wicket.lambda.WicketConsumer;
import org.apache.wicket.model.IModel;

/**
 * Created by Jorandeboever
 * Date: 29-Dec-16.
 */
class ActiesColumn<T, S> extends CustomColumn<T, S> {

    ActiesColumn(IModel<String> displayModel, WicketConsumer<IModel<T>> editConsumer, WicketConsumer<IModel<T>> deleteConsumer ) {
        super(displayModel,  (s, model) -> new EditDeleteCell<T>(s, model, editConsumer, deleteConsumer));
    }
}
