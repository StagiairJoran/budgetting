package be.ghostwritertje.webapp.form;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Created by Jorandeboever
 * Date: 25-Dec-16.
 */
public class NumberTextFieldComponentBuilder extends FormComponentBuilder<NumberTextField<?>, Double, NumberTextFieldComponentBuilder>  {
    NumberTextFieldComponentBuilder() {
    }


    @Override
    NumberTextField<?> buildFormComponent(String id, IModel<Double> model) {
        return new NumberTextField<>(id, model);
    }
}
