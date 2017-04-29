package be.ghostwritertje.webapp;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;

/**
 * Created by Jorandeboever
 * Date: 19-Dec-16.
 */
public class LambdaOnChangeBehavior extends OnChangeAjaxBehavior {

    private final SerializableBiConsumer<Component, AjaxRequestTarget> consumer;

    public LambdaOnChangeBehavior(SerializableBiConsumer<Component, AjaxRequestTarget> consumer) {
        this.consumer = consumer;
    }



    @Override
    protected void onUpdate(AjaxRequestTarget target) {
        consumer.accept(this.getComponent(), target);
    }
}
