package be.ghostwritertje.webapp.link;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.lambda.WicketFunction;
import org.apache.wicket.lambda.WicketSupplier;
import org.apache.wicket.markup.html.link.AbstractLink;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Jorandeboever
 * Date: 24-Dec-16.
 */
public abstract class LinkBuilderSupport<L extends LinkBuilderSupport<L, F>, F extends AbstractLink> {

    private WicketFunction<String, Component> iconProvider;
    private final List<WicketFunction<F, ? extends Behavior>> behaviors = new ArrayList<>();

    public LinkBuilderSupport() {
    }

    public L usingDefaults() {
        return (L) this.self();
    }

    public L icon(WicketFunction<String, Component> iconSupplier) {
        //TODO should do .andThen() -> Custom wicketconsumer?
        this.iconProvider = iconSupplier;
        return this.self();
    }

    public L behave(WicketSupplier<? extends Behavior> behavior) {
        this.behaviors.add(components -> behavior.get());
        return this.self();
    }

    @SuppressWarnings("unchecked")
    private L self() {
        return (L) this;
    }

    abstract F buildLink(String id);

    public L attach(MarkupContainer initialParent, String id) {
        F link = this.buildLink(id);
        Optional.ofNullable(this.iconProvider).ifPresent(ip -> link.add(ip.apply("icon")));
        this.behaviors.forEach(f -> link.add(f.apply(link)));
        initialParent.add(link.setOutputMarkupPlaceholderTag(true));
        return this.self();
    }
}
