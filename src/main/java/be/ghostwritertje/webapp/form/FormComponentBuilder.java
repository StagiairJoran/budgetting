package be.ghostwritertje.webapp.form;

import be.ghostwritertje.webapp.VisibilityBehavior;
import com.googlecode.wickedcharts.wicket7.highcharts.Chart;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.lambda.WicketBiFunction;
import org.apache.wicket.lambda.WicketFunction;
import org.apache.wicket.lambda.WicketSupplier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by Jorandeboever
 * Date: 23-Dec-16.
 */
public abstract class FormComponentBuilder<X extends FormComponent<T>, T extends Serializable> {

    private WicketBiFunction<String, IModel<String>, Component> labelSupplier = Label::new;
    private boolean switchable = false;
    private WicketSupplier<IModel<String>> labelModel = Model::new;

    public FormComponentBuilder usingDefaults() {
        this.switchable = true;
        return this.self();
    }

    private FormComponentBuilder self() {
        return this;
    }

    abstract X buildFormComponent(String id, IModel<T> model);

    public FormComponentBuilder attach(MarkupContainer initialParent, String id, IModel<T> model) {
        Component label = this.labelSupplier.apply(id + "-label", labelModel.get());
        Component readLabel = new Label(id + "-read", model);
        X formComponent = this.buildFormComponent(id, model);

        if (switchable) {
            formComponent.add(new VisibilityBehavior<>(component -> component.findParent(BaseForm.class).getFormModeModel().getObject().equals(BaseForm.FormMode.EDIT)));
            readLabel.add(new VisibilityBehavior<>(component -> component.findParent(BaseForm.class).getFormModeModel().getObject().equals(BaseForm.FormMode.READ)));
        }
        initialParent.add(readLabel);
        initialParent.add(label);
        initialParent.add(formComponent);
        return this.self();
    }

    public FormComponentBuilder body(ResourceModel labelModel) {
        this.labelModel = () -> labelModel;
        return this.self();
    }
}
