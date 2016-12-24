package be.ghostwritertje.webapp.form;

import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

/**
 * Created by Jorandeboever
 * Date: 24-Dec-16.
 */
public class PasswordTextFieldComponentBuilder extends FormComponentBuilder<PasswordTextField, String, PasswordTextFieldComponentBuilder> {
    PasswordTextFieldComponentBuilder() {
    }

    @Override
    PasswordTextField buildFormComponent(String id, IModel<String> model) {
        return new PasswordTextField(id, model);
    }
}
