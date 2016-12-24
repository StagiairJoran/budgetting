package be.ghostwritertje.webapp;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;

/**
 * Created by Jorandeboever
 * Date: 24-Dec-16.
 */
public class VisibilityBehaviorSupport extends Behavior {

    @Override
    public void bind(Component component) {
        super.bind(component);

        if(component.getBehaviors(ResetVisibilityAllowedBehavior.class).isEmpty()){
            component.remove(this);
            component.add(new ResetVisibilityAllowedBehavior());
            component.add(this);
        }
    }

    private static final class ResetVisibilityAllowedBehavior extends Behavior {
        @Override
        public void onConfigure(Component component) {
            super.onConfigure(component);
            component.setVisibilityAllowed(true);
        }
    }
}
