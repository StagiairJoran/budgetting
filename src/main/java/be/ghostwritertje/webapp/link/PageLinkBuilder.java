package be.ghostwritertje.webapp.link;

import org.apache.wicket.Page;
import org.apache.wicket.lambda.WicketFunction;
import org.apache.wicket.lambda.WicketSupplier;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Created by Jorandeboever
 * Date: 12-Jan-17.
 */
public class PageLinkBuilder<T> extends LinkBuilderSupport<PageLinkBuilder<T>, Link<T>> {
    private final WicketFunction<IModel<T> , ? extends Page> pageFunction;
    private  WicketSupplier<IModel<T>> modelSupplier;

    public PageLinkBuilder(WicketSupplier<? extends Page> supplier) {
        super();
        this.pageFunction = model -> supplier.get();
    }

    public PageLinkBuilder(WicketSupplier<IModel<T>> modelSupplier, WicketFunction<IModel<T> , ? extends Page> pageFunction){
        this.modelSupplier = modelSupplier;
        this.pageFunction = pageFunction;
    }

    @Override
    Link<T> buildLink(String id) {
        return new MyPageLink<T>(id, Optional.ofNullable(this.modelSupplier).map(Supplier::get).orElse(null), this.pageFunction);
    }

    private static class MyPageLink<T> extends Link<T>{
        private final  WicketFunction<IModel<T> , ? extends Page> pageFunction;

        public MyPageLink(String id, IModel<T> model,  WicketFunction<IModel<T> , ? extends Page> pageFunction) {
            super(id, model);
            this.pageFunction = pageFunction;
        }

        @Override
        public void onClick() {
            this.setResponsePage(pageFunction.apply(this.getModel()));
        }
    }
}
