package be.ghostwritertje.webapp.link;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;
import org.danekja.java.util.function.serializable.SerializableSupplier;

/**
 * Created by Jorandeboever
 * Date: 29-Dec-16.
 */
public class AjaxLinkBuilder <X> extends AjaxLinkBuilderSupport<AjaxLinkBuilder<X>, AjaxLink<X>> {

    AjaxLinkBuilder(SerializableBiConsumer<AjaxRequestTarget, AjaxLink<X>> onClickConsumer) {
        super(onClickConsumer);
    }
    private SerializableSupplier<IModel<X>> modelSupplier = () -> null;

    @Override
    AjaxLink<X> buildLink(String id, SerializableBiConsumer<AjaxRequestTarget, AjaxLink<X>> onClickConsumer) {
        return new MyAjaxLink<>(id, modelSupplier.get(), onClickConsumer);
    }

    private static class MyAjaxLink<X> extends AjaxLink<X>{
        private final SerializableBiConsumer<AjaxRequestTarget, AjaxLink<X>> consumer;

        private MyAjaxLink(String id, IModel<X> model, SerializableBiConsumer<AjaxRequestTarget, AjaxLink<X>> consumer) {
            super(id, model);
            this.consumer = consumer;
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
            this.consumer.accept(target, this);
        }
    }

    public AjaxLink<X> build(String id, IModel<X> model){
        this.modelSupplier = () -> model;
       return this.build(id);
    }

    public AjaxLinkBuilder<X> attach(MarkupContainer initialParent, String id, IModel<X> model) {
        this.modelSupplier = () -> model;
        return super.attach(initialParent, id);
    }
}
