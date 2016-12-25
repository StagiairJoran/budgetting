package be.ghostwritertje.webapp.form;

import be.ghostwritertje.webapp.LocalDateTextField;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.IModel;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created by Jorandeboever
 * Date: 25-Dec-16.
 */
public class LocalDateTextFieldComponentBuilder  extends FormComponentBuilder<LocalDateTextField, LocalDate, LocalDateTextFieldComponentBuilder> {
    LocalDateTextFieldComponentBuilder() {
    }

    @Override
    LocalDateTextField buildFormComponent(String id, IModel<LocalDate> model) {
        return new LocalDateTextField(id, model);
    }
}
