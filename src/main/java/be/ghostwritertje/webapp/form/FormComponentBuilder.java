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
public abstract class FormComponentBuilder<X extends FormComponent<T>, T extends Serializable, F extends FormComponentBuilder> {

    private WicketBiFunction<String, IModel<String>, Component> labelSupplier = Label::new;
    private boolean switchable = true;
    private WicketSupplier<IModel<String>> labelModel = Model::new;
    private boolean required = false;

    public F usingDefaults() {
        this.switchable = true;
        return this.self();
    }

    public F switchable(boolean switchable){
        this.switchable = switchable;
        return this.self();
    }

    public F notRequired(){
        this.required = false;
        return this.self();
    }

    public F required(){
        this.required = true;
        return this.self();
    }

    @SuppressWarnings("unchecked")
    private F self() {
        return (F) this;
    }

    abstract X buildFormComponent(String id, IModel<T> model);

    public F attach(MarkupContainer initialParent, String id, IModel<T> model) {
        Component label = this.labelSupplier.apply(id + "-label", labelModel.get());
        X formComponent = this.buildFormComponent(id, model);

        formComponent.setRequired(required);

        if (switchable) {
            Component readLabel = new Label(id + "-read", model);
            initialParent.add(readLabel.setOutputMarkupPlaceholderTag(true));
            formComponent.add(new VisibilityBehavior<>(component -> component.findParent(BaseForm.class).getFormModeModel().getObject().equals(BaseForm.FormMode.EDIT)));
            readLabel.add(new VisibilityBehavior<>(component -> component.findParent(BaseForm.class).getFormModeModel().getObject().equals(BaseForm.FormMode.READ)));
        }
        initialParent.add(label.setOutputMarkupPlaceholderTag(true));
        initialParent.add(formComponent.setOutputMarkupPlaceholderTag(true));
        return this.self();
    }

    public F body(ResourceModel labelModel) {
        this.labelModel = () -> labelModel;
        return this.self();
    }
}
