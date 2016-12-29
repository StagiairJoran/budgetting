package be.ghostwritertje.webapp.datatable;

import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.lambda.WicketBiConsumer;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

/**
 * Created by Jorandeboever
 * Date: 29-Dec-16.
 */
class EditDeleteCell<T> extends GenericPanel<T> {
    private final WicketBiConsumer<AjaxRequestTarget, AjaxLink<T>> editConsumer;
    private final WicketBiConsumer<AjaxRequestTarget, AjaxLink<T>> deleteConsumer;

    EditDeleteCell(String id, IModel<T> model, WicketBiConsumer<AjaxRequestTarget, AjaxLink<T>> editConsumer, WicketBiConsumer<AjaxRequestTarget, AjaxLink<T>> deleteConsumer) {
        super(id, model);
        this.editConsumer = editConsumer;
        this.deleteConsumer = deleteConsumer;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        LinkBuilderFactory.editLink(editConsumer)
                .usingDefaults()
                .attach(this, "edit", this.getModel());

        LinkBuilderFactory.deleteLink(deleteConsumer)
                .usingDefaults()
                .attach(this, "delete");

    }
}
