package be.ghostwritertje.webapp.link;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.lambda.WicketBiConsumer;
import org.apache.wicket.lambda.WicketFunction;
import org.apache.wicket.markup.html.link.AbstractLink;

import java.util.Optional;

/**
 * Created by Jorandeboever
 * Date: 24-Dec-16.
 */
public abstract class LinkBuilderSupport<L extends LinkBuilderSupport<L, F>, F extends AbstractLink> {

    private WicketFunction<String, Component> iconProvider;
    private final WicketBiConsumer<AjaxRequestTarget, F> onClickConsumer;

    public LinkBuilderSupport(WicketBiConsumer<AjaxRequestTarget, F> onClickConsumer) {
        this.onClickConsumer = onClickConsumer;
    }

    public L usingDefaults() {
        return (L) this.self();
    }

    public L icon(WicketFunction<String, Component> iconSupplier) {
        //TODO should do .andThen() -> Custom wicketconsumer?
        this.iconProvider = iconSupplier;
        return this.self();
    }

    @SuppressWarnings("unchecked")
    private L self() {
        return (L) this;
    }

    abstract F buildLink(String id, WicketBiConsumer<AjaxRequestTarget, F> onClickConsumer);

    public L attach(MarkupContainer initialParent, String id) {
        F link = this.buildLink(id, this.onClickConsumer);
        Optional.ofNullable(iconProvider).ifPresent(ip -> link.add(ip.apply("icon") ));
        initialParent.add(link.setOutputMarkupPlaceholderTag(true));
        return this.self();
    }
}
