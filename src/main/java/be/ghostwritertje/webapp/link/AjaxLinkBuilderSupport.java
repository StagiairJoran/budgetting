package be.ghostwritertje.webapp.link;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;

/**
 * Created by Jorandeboever
 * Date: 12-Jan-17.
 */
public abstract class AjaxLinkBuilderSupport<L extends LinkBuilderSupport<L, F>, F extends AbstractLink> extends LinkBuilderSupport<L, F> {
    private final SerializableBiConsumer<AjaxRequestTarget, F> onClickConsumer;


    public AjaxLinkBuilderSupport(SerializableBiConsumer<AjaxRequestTarget, F> onClickConsumer) {
        super();
        this.onClickConsumer = onClickConsumer;
    }

    abstract F buildLink(String id, SerializableBiConsumer<AjaxRequestTarget, F> onClickConsumer);

     F buildLink(String id){
         return this.buildLink(id, this.onClickConsumer);
     }

}
