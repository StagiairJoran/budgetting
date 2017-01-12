package be.ghostwritertje.webapp.link;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.lambda.WicketBiConsumer;
import org.apache.wicket.lambda.WicketSupplier;
import org.apache.wicket.model.IModel;

/**
 * Created by Jorandeboever
 * Date: 29-Dec-16.
 */
public class AjaxLinkBuilder <X> extends AjaxLinkBuilderSupport<AjaxLinkBuilder<X>, AjaxLink<X>> {

    AjaxLinkBuilder(WicketBiConsumer<AjaxRequestTarget, AjaxLink<X>> onClickConsumer) {
        super(onClickConsumer);
    }
    private WicketSupplier<IModel<X>> modelSupplier = () -> null;

    @Override
    AjaxLink<X> buildLink(String id, WicketBiConsumer<AjaxRequestTarget, AjaxLink<X>> onClickConsumer) {
        return new MyAjaxLink<>(id, modelSupplier.get(), onClickConsumer);
    }

    private static class MyAjaxLink<X> extends AjaxLink<X>{
        private final WicketBiConsumer<AjaxRequestTarget, AjaxLink<X>> consumer;

        private MyAjaxLink(String id, IModel<X> model, WicketBiConsumer<AjaxRequestTarget, AjaxLink<X>> consumer) {
            super(id, model);
            this.consumer = consumer;
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
            this.consumer.accept(target, this);
        }
    }

    public AjaxLinkBuilder<X> attach(MarkupContainer initialParent, String id, IModel<X> model) {
        this.modelSupplier = () -> model;
        return super.attach(initialParent, id);
    }
}
