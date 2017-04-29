package be.ghostwritertje.webapp.link;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.danekja.java.util.function.serializable.SerializableSupplier;

/**
 * Created by Jorandeboever
 * Date: 24-Dec-16.
 */
public class LinkBuilderFactory {
    public static AjaxSubmitLinkBuilder submitLink(SerializableBiConsumer<AjaxRequestTarget, AjaxSubmitLink> onClickConsumer) {
        return new AjaxSubmitLinkBuilder(onClickConsumer);
    }

    public static <X> AjaxLinkBuilder<X> ajaxLink(SerializableBiConsumer<AjaxRequestTarget, AjaxLink<X>> onClickConsumer) {
        return new AjaxLinkBuilder<>(onClickConsumer);
    }

    public static <X> AjaxLinkBuilder<X> editLink(SerializableBiConsumer<AjaxRequestTarget, AjaxLink<X>> consumer) {
        return new AjaxLinkBuilder<X>(consumer)
                .icon(o -> new Icon(o, FontAwesomeIconTypeBuilder.on(FontAwesomeIconTypeBuilder.FontAwesomeGraphic.edit).size(FontAwesomeIconTypeBuilder.Size.two).build()));
    }

    public static <X> AjaxLinkBuilder<X> deleteLink(SerializableBiConsumer<AjaxRequestTarget, AjaxLink<X>> consumer) {
        return new AjaxLinkBuilder<X>(consumer)
                .icon(o -> new Icon(o, FontAwesomeIconTypeBuilder.on(FontAwesomeIconTypeBuilder.FontAwesomeGraphic.trash_o).size(FontAwesomeIconTypeBuilder.Size.two).build()));
    }

    public static <X> PageLinkBuilder<X> pageLink(SerializableSupplier<IModel<X>> modelSupplier, SerializableFunction<IModel<X> , ? extends Page> pageFunction){
        return new PageLinkBuilder<X>(modelSupplier, pageFunction);
    }

    public static <X> PageLinkBuilder<X> pageLink(SerializableSupplier<? extends Page> supplier){
        return new PageLinkBuilder<X>(supplier);
    }
}
