package be.ghostwritertje.webapp.link;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.lambda.WicketBiConsumer;
import org.apache.wicket.markup.html.link.AbstractLink;

/**
 * Created by Jorandeboever
 * Date: 12-Jan-17.
 */
public abstract class AjaxLinkBuilderSupport<L extends LinkBuilderSupport<L, F>, F extends AbstractLink> extends LinkBuilderSupport<L, F> {
    private final WicketBiConsumer<AjaxRequestTarget, F> onClickConsumer;


    public AjaxLinkBuilderSupport(WicketBiConsumer<AjaxRequestTarget, F> onClickConsumer) {
        super();
        this.onClickConsumer = onClickConsumer;
    }

    abstract F buildLink(String id, WicketBiConsumer<AjaxRequestTarget, F> onClickConsumer);

     F buildLink(String id){
         return this.buildLink(id, this.onClickConsumer);
     }

}
