package be.ghostwritertje.webapp;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.lambda.WicketBiConsumer;

/**
 * Created by Jorandeboever
 * Date: 19-Dec-16.
 */
public class LambdaOnChangeBehavior extends OnChangeAjaxBehavior {

    private final WicketBiConsumer<Component, AjaxRequestTarget> consumer;

    public LambdaOnChangeBehavior(WicketBiConsumer<Component, AjaxRequestTarget> consumer) {
        this.consumer = consumer;
    }



    @Override
    protected void onUpdate(AjaxRequestTarget target) {
        consumer.accept(this.getComponent(), target);
    }
}
