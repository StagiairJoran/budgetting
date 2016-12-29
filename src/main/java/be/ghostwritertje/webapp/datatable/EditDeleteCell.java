package be.ghostwritertje.webapp.datatable;

import org.apache.wicket.lambda.WicketConsumer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

/**
 * Created by Jorandeboever
 * Date: 29-Dec-16.
 */
class EditDeleteCell<T> extends GenericPanel<T> {
    private final WicketConsumer<IModel<T>> editConsumer;
    private final WicketConsumer<IModel<T>> deleteConsumer;

    EditDeleteCell(String id, IModel<T> model, WicketConsumer<IModel<T>> editConsumer, WicketConsumer<IModel<T>> deleteConsumer) {
        super(id, model);
        this.editConsumer = editConsumer;
        this.deleteConsumer = deleteConsumer;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        this.add(new Link<T>("edit", this.getModel()) {
            @Override
            public void onClick() {
                editConsumer.accept(this.getModel());
            }
        });

        this.add(new Link<T>("delete", this.getModel()) {
            @Override
            public void onClick() {
                deleteConsumer.accept(this.getModel());
            }
        });
    }
}
