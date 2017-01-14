package be.ghostwritertje.webapp;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.webapp.person.pages.LoginPage;
import be.ghostwritertje.webapp.person.pages.LogoutPage;
import be.ghostwritertje.webapp.person.pages.RegisterPage;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCssReference;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;

/**
 * Created by Ghostwritertje
 * Date: 30-Sep-16.
 */
public abstract class BasePage<T> extends GenericWebPage<T>  implements AuthorizationRequired {
    protected BasePage() {
        super();
    }

    protected BasePage(IModel<T> model) {
        super(model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        this.add(new BookmarkablePageLink("homeLink", DashboardPage.class));
//        this.add(new BookmarkablePageLink("usersLink", PersonListPage.class));
//        this.add(new BookmarkablePageLink("funds", FinancialInstrumentListPage.class));
//        this.add(new BookmarkablePageLink("portfolioLink", PortfolioListPage.class));

        this.add(new BookmarkablePageLink("registerLink", RegisterPage.class)
                .add(new VisibilityBehavior<>(component -> CustomSession.get().getLoggedInPerson() == null)));
        this.add(new BookmarkablePageLink("loginLink", LoginPage.class)
                .add(new VisibilityBehavior<>(component -> CustomSession.get().getLoggedInPerson() == null)));

        this.add(new Link<Person>("dashboardLink", new LambdaModel<>(() -> CustomSession.get().getLoggedInPerson(), person -> CustomSession.get().setLoggedInPerson(person))) {

            @Override
            protected void onInitialize() {
                super.onInitialize();
                this.add(new VisibilityBehavior<>(component -> CustomSession.get().getLoggedInPerson() != null));
                this.add(new Label("loggedInUsername", new LambdaModel<>(() -> this.getModelObject().getUsername(), s -> this.getModelObject().setUsername(s))));
            }

            @Override
            public void onClick() {
                this.setResponsePage(new DashboardPage(this.getModel()));

            }
        });
//        this.add(new Link<Person>("fundPurchasesLink") {
//            @Override
//            public void onClick() {
//                this.setResponsePage(FundPurchaseListPage.class);
//            }
//        });

        this.add(new Link<Person>("logoutLink") {
            @Override
            public void onClick() {
                CustomSession.get().invalidate();
                this.setResponsePage(LogoutPage.class);
            }
        });

//        this.add(new Link<T>("statementsLink", this.getModel()) {
//            @Override
//            public void onClick() {
//                this.setResponsePage(new StatementListPage(new Model<Person>(CustomSession.get().getLoggedInPerson())));
//            }
//
//            @Override
//            protected void onInitialize() {
//                super.onInitialize();
//                this.add(new VisibilityBehavior<>(component -> CustomSession.get().getLoggedInPerson() != null));
//            }
//        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(new CssReferenceHeaderItem(FontAwesomeCssReference.instance(), null, null, null));
    }
}
