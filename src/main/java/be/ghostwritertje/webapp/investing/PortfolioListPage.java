package be.ghostwritertje.webapp.investing;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.investing.FundPurchase;
import be.ghostwritertje.domain.investing.Portfolio;
import be.ghostwritertje.services.investing.PortfolioService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.model.DomainObjectListModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 28-Dec-16.
 */
public class PortfolioListPage extends BasePage<Person> {

    @SpringBean
    private PortfolioService portfolioService;
    private final IModel<List<Portfolio>> portfolioListModel;

    public PortfolioListPage() {
        this.portfolioListModel = new DomainObjectListModel<Portfolio>(this.portfolioService);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        this.add(new BookmarkablePageLink<>("newPortfolio", PortfolioDetailPage.class));
        this.add(new ListView<Portfolio>("portfolios", this.portfolioListModel) {
            @Override
            protected void onInitialize() {
                super.onInitialize();
                this.setViewSize(25);
            }
            @Override
            protected void populateItem(ListItem<Portfolio> item) {
                item.add(new Label("name", item.getModelObject().getName()));

                item.add(new Link<Portfolio>("link", item.getModel()) {
                    @Override
                    public void onClick() {
                        this.setResponsePage(new PortfolioDetailPage(item.getModel()));
                    }
                });

            }
        });
    }
}
