package be.ghostwritertje.webapp.datatable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.lambda.WicketBiConsumer;
import org.apache.wicket.model.IModel;

/**
 * Created by Jorandeboever
 * Date: 29-Dec-16.
 */
class ActiesColumn<T, S> extends CustomColumn<T, S> {

    ActiesColumn(IModel<String> displayModel, WicketBiConsumer<AjaxRequestTarget, AjaxLink<T>> editConsumer, WicketBiConsumer<AjaxRequestTarget, AjaxLink<T>> deleteConsumer ) {
        super(displayModel,  (s, model) -> new EditDeleteCell<T>(s, model, editConsumer, deleteConsumer));
    }
}
