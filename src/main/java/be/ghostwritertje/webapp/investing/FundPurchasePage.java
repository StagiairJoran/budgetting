package be.ghostwritertje.webapp.investing;

import be.ghostwritertje.domain.investing.FinancialInstrument;
import be.ghostwritertje.domain.investing.FundPurchase;
import be.ghostwritertje.services.investing.FinanceService;
import be.ghostwritertje.services.investing.FinancialInstrumentService;
import be.ghostwritertje.services.investing.FundPurchaseService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.form.BaseForm;
import be.ghostwritertje.webapp.form.FormComponentBuilderFactory;
import be.ghostwritertje.webapp.link.LinkBuilderFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;

/**
 * Created by Jorandeboever
 * Date: 08-Oct-16.
 */
public class FundPurchasePage extends BasePage<FundPurchase> {

    @SpringBean
    private FundPurchaseService fundPurchaseService;

    @SpringBean
    private FinancialInstrumentService financialInstrumentService;

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
                .attach(form, "date", LambdaModel.of(this.getModel(), FundPurchase::getDate, FundPurchase::setDate));

        FormComponentBuilderFactory.<FinancialInstrument>dropDown()
                .usingDefaults()
                .body(new ResourceModel("quote"))
                .attach(form,
                        "quote",
                        LambdaModel.of(this.getModel(), FundPurchase::getFinancialInstrument, FundPurchase::setFinancialInstrument),
                        () -> this.financialInstrumentService.findAll());

        FormComponentBuilderFactory.number(Double.class)
                .usingDefaults()
                .body(new ResourceModel("share.price"))
                .attach(
                        form,
                        "sharePrice",
                        LambdaModel.of(this.getModel(), FundPurchase::getSharePrice, FundPurchase::setSharePrice)
                )
                .body(new ResourceModel("transaction.cost"))
                .attach(
                        form,
                        "transactionCost",
                        LambdaModel.of(this.getModel(), FundPurchase::getTransactionCost, FundPurchase::setTransactionCost)
                );
        FormComponentBuilderFactory.number(Integer.class)
                .body(new ResourceModel("count"))
                .attach(form, "count",  LambdaModel.of(this.getModel(), FundPurchase::getNumberOfShares, FundPurchase::setNumberOfShares));


        LinkBuilderFactory.submitLink(save())
                .usingDefaults()
                .attach(form, "save");

        this.add(form);
    }

    private static SerializableBiConsumer<AjaxRequestTarget, AjaxSubmitLink> save() {
        return (target, components) -> {
            FundPurchasePage parent = components.findParent(FundPurchasePage.class);
            parent.setModelObject(parent.fundPurchaseService.save(parent.getModelObject()));
        };
    }


}
