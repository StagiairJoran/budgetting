package be.ghostwritertje.webapp.link;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconTypeBuilder;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.lambda.WicketBiConsumer;

/**
 * Created by Jorandeboever
 * Date: 24-Dec-16.
 */
public class LinkBuilderFactory {
    public static AjaxSubmitLinkBuilder submitLink(WicketBiConsumer<AjaxRequestTarget, AjaxSubmitLink> onClickConsumer) {
        return new AjaxSubmitLinkBuilder(onClickConsumer);
    }

    public static <X> AjaxLinkBuilder ajaxLink(WicketBiConsumer<AjaxRequestTarget, AjaxLink<X>> onClickConsumer) {
        return new AjaxLinkBuilder<>(onClickConsumer);
    }

    public static <X> AjaxLinkBuilder<X> editLink(WicketBiConsumer<AjaxRequestTarget, AjaxLink<X>> consumer) {
        return new AjaxLinkBuilder<X>(consumer)
                .icon(o -> new Icon(o, FontAwesomeIconTypeBuilder.on(FontAwesomeIconTypeBuilder.FontAwesomeGraphic.edit).size(FontAwesomeIconTypeBuilder.Size.two).build()));
    }

    public static <X> AjaxLinkBuilder<X> deleteLink(WicketBiConsumer<AjaxRequestTarget, AjaxLink<X>> consumer) {
        return new AjaxLinkBuilder<X>(consumer)
                .icon(o -> new Icon(o, FontAwesomeIconTypeBuilder.on(FontAwesomeIconTypeBuilder.FontAwesomeGraphic.trash_o).size(FontAwesomeIconTypeBuilder.Size.two).build()));
    }
}
