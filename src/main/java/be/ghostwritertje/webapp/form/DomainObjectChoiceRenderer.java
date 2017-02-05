package be.ghostwritertje.webapp.form;

import be.ghostwritertje.domain.DomainObject;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 05-Feb-17.
 */
public class DomainObjectChoiceRenderer implements IChoiceRenderer<DomainObject> {
    @Override
    public Object getDisplayValue(DomainObject object) {
        return object.getDisplayValue();
    }

    @Override
    public String getIdValue(DomainObject object, int index) {
        return object.getUuid();
    }

    @Override
    public DomainObject getObject(String id, IModel<? extends List<? extends DomainObject>> choices) {
        return choices.getObject().stream().filter(o ->  o.getUuid().equals(id)).findFirst().orElse(null);
    }
}
