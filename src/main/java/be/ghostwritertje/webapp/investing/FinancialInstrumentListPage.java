package be.ghostwritertje.webapp.investing;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.investing.FinancialInstrument;
import be.ghostwritertje.domain.investing.FundPurchase;
import be.ghostwritertje.services.investing.FinancialInstrumentService;
import be.ghostwritertje.webapp.BasePage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 26-Dec-16.
 */
public class FinancialInstrumentListPage extends BasePage<Person> {

    @SpringBean
    private FinancialInstrumentService financialInstrumentService;
    private final IModel<List<FinancialInstrument>> financialInstrumentListModel;

    public FinancialInstrumentListPage() {
        this.financialInstrumentListModel = new ListModel<>(this.financialInstrumentService.findAll());
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        this.add(new ListView<FinancialInstrument>("financialInstruments", this.financialInstrumentListModel) {
            @Override
            protected void onInitialize() {
                super.onInitialize();
                this.setViewSize(25);
            }

            @Override
            protected void populateItem(ListItem<FinancialInstrument> item) {
                item.add(new Label("quote", item.getModelObject().getQuote()));
                item.add(new Label("yearToDateReturn", item.getModelObject().getYearToDateReturn()));
                item.add(new Label("fiveYearReturn", item.getModelObject().get5yearReturn()));
                item.add(new Label("currentPrice", item.getModelObject().getCurrentPrice()));
                item.add(new Link<FinancialInstrument>("link", item.getModel()) {
                    @Override
                    public void onClick() {
                        setResponsePage(new FinancialInstrumentPage(item.getModel()));
                    }
                });
            }
        });
    }
}
