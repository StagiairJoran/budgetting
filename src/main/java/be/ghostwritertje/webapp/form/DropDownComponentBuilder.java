package be.ghostwritertje.webapp.form;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableSupplier;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 05-Feb-17.
 */
public class DropDownComponentBuilder<T extends Display & Serializable> extends FormComponentBuilder<DropDownChoice<T>, T, DropDownComponentBuilder<T>> {

    private SerializableSupplier<List<T>> listSupplier;

    @Override
    public DropDownComponentBuilder<T> attach(MarkupContainer initialParent, String id, IModel<T> model) {
        throw new IllegalArgumentException();
    }

    public DropDownComponentBuilder<T> attach(MarkupContainer initialParent, String id, IModel<T> model, SerializableSupplier<List<T>> listSupplier) {
        this.listSupplier = listSupplier;
        super.attach(initialParent, id, model);

        return this.self();
    }

    @Override
    DropDownChoice<T> buildFormComponent(String id, IModel<T> model) {
        return new DropDownChoice<>(
                id,
                model,
                this.listSupplier.get(),
                new CustomChoiceRenderer()
        );
    }
}
