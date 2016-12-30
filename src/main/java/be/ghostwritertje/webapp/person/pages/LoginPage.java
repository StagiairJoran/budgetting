package be.ghostwritertje.webapp.person.pages;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.services.person.PersonService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.CustomSession;
import be.ghostwritertje.webapp.DashboardPage;
import be.ghostwritertje.webapp.UnAuthorizedAllowed;
import be.ghostwritertje.webapp.form.BaseForm;
import be.ghostwritertje.webapp.form.FormComponentBuilderFactory;
import be.ghostwritertje.webapp.person.PersonModel;
import org.apache.log4j.Logger;
import org.apache.wicket.authentication.IAuthenticationStrategy;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Optional;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class LoginPage extends BasePage<Person> implements UnAuthorizedAllowed {
    @SpringBean
    private PersonService personService;

    private static final Logger logger = Logger.getLogger(LoginPage.class);

    public LoginPage() {
        super(new Model<>(new Person()));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        Form<Person> form = new LoginForm("form", this.getModel());
        this.add(form);
    }


    class LoginForm extends BaseForm<Person> {

        private final IModel<Boolean> rememberMe;

        public LoginForm(String id, IModel<Person> model) {
            super(id, model);
            this.rememberMe = new Model<>(true);
        }

        @Override
        public void onSubmit() {
            super.onSubmit();
            Optional.ofNullable(LoginPage.this.personService.logIn(LoginPage.this.getModelObject())).ifPresent(this::login);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            FormComponentBuilderFactory.textField()
                    .usingDefaults()
                    .required()
                    .body(new ResourceModel("username"))
                    .attach(this, "username", new LambdaModel<>(() -> this.getModel().getObject().getUsername(), username -> this.getModel().getObject().setUsername(username)));

            FormComponentBuilderFactory.password()
                    .usingDefaults()
                    .switchable(false)
                    .required()
                    .body(new ResourceModel("password"))
                    .attach(this, "password", new LambdaModel<>(() -> this.getModelObject().getPassword(), password -> this.getModelObject().setPassword(password)));

            WebMarkupContainer rememberMeContainer = new WebMarkupContainer("rememberMeContainer");
            this.add(rememberMeContainer);

            rememberMeContainer.add(new CheckBox("rememberMe", this.rememberMe));
            this.add(new SubmitLink("save"));
        }

        private void login(Person person) {
            IAuthenticationStrategy strategy = getApplication().getSecuritySettings()
                    .getAuthenticationStrategy();

            if (AuthenticatedWebSession.get().signIn(person.getUsername(), person.getUsername())) {
                CustomSession.get().setLoggedInPerson(person);
                if (this.rememberMe.getObject()) {
                    strategy.save(person.getUsername(), person.getUsername());
                } else {
                    strategy.remove();
                }
                logger.info(String.format("User %s has logged in", person.getUsername()));
                this.setResponsePage(new DashboardPage(new PersonModel(new Model<String>(person.getUuid()))));
            } else {
                error(getLocalizer().getString("signInFailed", this, "Sign in failed"));
            }
        }
    }
}
