package be.ghostwritertje.webapp.investing;

import be.ghostwritertje.domain.investing.FinancialInstrument;
import be.ghostwritertje.webapp.BasePage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * Created by Jorandeboever
 * Date: 26-Dec-16.
 */
public class FinancialInstrumentPage extends BasePage<FinancialInstrument> {


    public FinancialInstrumentPage(IModel<FinancialInstrument> model) {
        super(model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        this.add(new Label("quote", this.getModelObject().getQuote()));
        this.add(new Label("yearToDateReturn", this.getModelObject().getYearToDateReturn()));
        this.add(new Label("fiveYearReturn", this.getModelObject().get5yearReturn()));
        this.add(new Label("currentPrice", this.getModelObject().getCurrentPrice()));
    }
}
