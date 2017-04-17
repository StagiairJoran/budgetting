package be.ghostwritertje.webapp;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializablePredicate;

import java.util.function.Predicate;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class IModelBasedVisibilityBehavior<T> extends VisibilityBehaviorSupport {

    private final IModel<T> entityModel;
    private final Predicate<T> visibilityLogic;

    public IModelBasedVisibilityBehavior(IModel<T> entityModel, SerializablePredicate<T> visibilityLogic) {
        this.entityModel = entityModel;
        this.visibilityLogic = visibilityLogic;
    }

    @Override
    public void onConfigure(Component component) {
        super.onConfigure(component);
        component.setVisibilityAllowed(component.isVisibilityAllowed() && this.visibilityLogic.test(this.entityModel.getObject()));
    }
}
