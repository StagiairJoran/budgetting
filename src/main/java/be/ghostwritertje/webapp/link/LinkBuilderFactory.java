package be.ghostwritertje.webapp.link;

/**
 * Created by Jorandeboever
 * Date: 24-Dec-16.
 */
public class LinkBuilderFactory {
    public static AjaxSubmitLinkBuilder submitLink(){
        return new AjaxSubmitLinkBuilder();
    }
}
