package be.ghostwritertje.webapp.form;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 05-Feb-17.
 */
public class CustomChoiceRenderer implements IChoiceRenderer<Display> {
    private static final long serialVersionUID = 5856685355531641364L;

    @Override
    public Object getDisplayValue(Display object) {
        return object.getDisplayValue();
    }

    @Override
    public String getIdValue(Display object, int index) {
        return object.getId();
    }

    @Override
    public Display getObject(String id, IModel<? extends List<? extends Display>> choices) {
        return choices.getObject().stream().filter(o ->  o.getId().equals(id)).findFirst().orElse(null);
    }
}
