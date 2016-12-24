package be.ghostwritertje.webapp.form;

import be.ghostwritertje.webapp.IModelBasedVisibilityBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;


/**
 * Created by Jorandeboever
 * Date: 23-Dec-16.
 */
public class TextFieldComponentBuilder extends FormComponentBuilder<TextField<String>, String> {
    TextFieldComponentBuilder() {
    }

    @Override
    TextField<String> buildFormComponent(String id, IModel<String> model) {
        return new TextField<String>(id, model);
    }
}
