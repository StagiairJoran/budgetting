package be.ghostwritertje.webapp.form;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkbox.bootstrapcheckbox.BootstrapCheckBoxPicker;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;

/**
 * Created by Jorandeboever on 5/7/2017.
 */
public class CheckBoxComponentBuilder extends FormComponentBuilder<CheckBox, Boolean, CheckBoxComponentBuilder> {
    @Override
    CheckBox buildFormComponent(String id, IModel<Boolean> model) {
        return new BootstrapCheckBoxPicker(id, model);
    }
}
