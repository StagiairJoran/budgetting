package be.ghostwritertje.webapp.investing;

import be.ghostwritertje.domain.investing.FundPurchase;
import be.ghostwritertje.services.investing.FinanceService;
import be.ghostwritertje.services.investing.FundPurchaseService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.LocalDateTextField;
import be.ghostwritertje.webapp.form.BaseForm;
import be.ghostwritertje.webapp.form.FormComponentBuilderFactory;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.lambda.WicketBiConsumer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by Jorandeboever
 * Date: 08-Oct-16.
 */
public class FundPurchasePage extends BasePage<FundPurchase> {

    @SpringBean
    private FundPurchaseService fundPurchaseService;

    @SpringBean
    private FinanceService financeService;

    public FundPurchasePage(IModel<FundPurchase> model) {
        super(model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        BaseForm<FundPurchase> form = new BaseForm<FundPurchase>("form", this.getModel());

        FormComponentBuilderFactory.date()
                .usingDefaults()
                .body(new ResourceModel("date"))
                .attach(form, "date", new LambdaModel<>(() -> this.getModel().getObject().getDate(), localDate -> this.getModel().getObject().setDate(localDate)));

        FormComponentBuilderFactory.textField()
                .usingDefaults()
                .body(new ResourceModel("quote"))
                .attach(form, "quote", new LambdaModel<String>(() -> this.getModelObject().getQuote(), quote -> this.getModelObject().setQuote(quote)));
        FormComponentBuilderFactory.number()
                .usingDefaults()
                .body(new ResourceModel("share.price"))
                .attach(form, "sharePrice", new LambdaModel<Double>(() -> this.getModelObject().getSharePrice(), sharePrice -> this.getModelObject().setSharePrice(sharePrice)))
                .body(new ResourceModel("count"))
                .attach(form, "count", new LambdaModel<Integer>(() -> this.getModelObject().getNumberOfShares(), count -> this.getModelObject().setNumberOfShares(count)));

        LinkBuilderFactory.submitLink()
                .usingDefaults()
                .attach(form, "save", save());

        this.add(form);
    }

    private static WicketBiConsumer<AjaxRequestTarget, AjaxSubmitLink> save() {
        return (target, components) -> {
            FundPurchasePage parent = components.findParent(FundPurchasePage.class);
            parent.setModelObject(parent.fundPurchaseService.save(parent.getModelObject()));
        };
    }

}
