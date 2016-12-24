package be.ghostwritertje.webapp.link;

import be.ghostwritertje.webapp.VisibilityBehavior;
import be.ghostwritertje.webapp.form.BaseForm;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.lambda.WicketBiConsumer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

/**
 * Created by Jorandeboever
 * Date: 24-Dec-16.
 */
public abstract class LinkBuilder <L extends LinkBuilder<L, F>, F extends AbstractLink> {

    public L usingDefaults() {
        return this.self();
    }

    @SuppressWarnings("unchecked")
    private L self() {
        return (L) this;
    }

    abstract F buildLink(String id,  WicketBiConsumer<AjaxRequestTarget, F> submitConsumer);

    public L attach(MarkupContainer initialParent, String id, WicketBiConsumer<AjaxRequestTarget, F> submitConsumer) {
        F link = this.buildLink(id, submitConsumer);

        initialParent.add(link.setOutputMarkupPlaceholderTag(true));
        return this.self();
    }
}
