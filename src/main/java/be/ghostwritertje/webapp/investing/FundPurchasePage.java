package be.ghostwritertje.webapp.investing;

import be.ghostwritertje.domain.investing.FundPurchase;
import be.ghostwritertje.services.investing.FinanceService;
import be.ghostwritertje.services.investing.FundPurchaseService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.LocalDateTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
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

        Form<FundPurchase> form = new Form<FundPurchase>("form", this.getModel()) {
            @Override
            public void onSubmit() {
                super.onSubmit();
                FundPurchase savedPurchase = FundPurchasePage.this.fundPurchaseService.save(FundPurchasePage.this.getModelObject());
                this.setResponsePage(new FundPurchaseListPage());
            }
        };

        form.add(new LocalDateTextField("date", new LambdaModel<>(() -> this.getModel().getObject().getDate(), localDate -> this.getModel().getObject().setDate(localDate))));
        form.add(new TextField<>("quote", new LambdaModel<String>(() -> this.getModelObject().getQuote(), quote -> this.getModelObject().setQuote(quote))));
        form.add(new NumberTextField<Double>("sharePrice", new LambdaModel<Double>(() -> this.getModelObject().getSharePrice(), sharePrice -> this.getModelObject().setSharePrice(sharePrice)), Double.class));
        form.add(new NumberTextField<Integer>("count", new LambdaModel<>(() -> this.getModelObject().getNumberOfShares(), count -> this.getModelObject().setNumberOfShares(count)), Integer.class));

        form.add(new SubmitLink("save"));

        this.add(form);
    }

}
