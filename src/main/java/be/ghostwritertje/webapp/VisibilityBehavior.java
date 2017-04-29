package be.ghostwritertje.webapp;

import org.apache.wicket.Component;
import org.danekja.java.util.function.serializable.SerializableFunction;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class VisibilityBehavior<X extends Component> extends VisibilityBehaviorSupport {

    private final SerializableFunction<? super X, Boolean> visibilityLogic;

    public VisibilityBehavior(SerializableFunction<? super X, Boolean> visibilityLogic) {
        this.visibilityLogic = visibilityLogic;
    }

    @Override
    public void onConfigure(Component component) {
        super.onConfigure(component);
        component.setVisibilityAllowed(component.isVisibilityAllowed() && this.visibilityLogic.apply((X) component));
    }
}
