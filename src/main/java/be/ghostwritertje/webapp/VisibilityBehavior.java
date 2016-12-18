package be.ghostwritertje.webapp;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.lambda.WicketFunction;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class VisibilityBehavior<X extends Component> extends Behavior {

    private final WicketFunction<? super X, Boolean> visibilityLogic;

    public VisibilityBehavior(WicketFunction<? super X, Boolean> visibilityLogic) {
        this.visibilityLogic = visibilityLogic;
    }

    @Override
    public void onConfigure(Component component) {
        super.onConfigure(component);
        component.setVisibilityAllowed(component.isVisibilityAllowed() && this.visibilityLogic.apply((X) component));
    }
}
