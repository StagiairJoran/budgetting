package be.ghostwritertje.webapp.person.pages;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.services.person.PersonService;
import be.ghostwritertje.webapp.BasePage;
import be.ghostwritertje.webapp.UnAuthorizedAllowed;
import be.ghostwritertje.webapp.form.BaseForm;
import be.ghostwritertje.webapp.form.FormComponentBuilderFactory;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class RegisterPage extends BasePage<Person> implements UnAuthorizedAllowed {

    @SpringBean
    private PersonService personService;

    public RegisterPage() {
        super(new Model<>(new Person()));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        Form<Person> form = new BaseForm<Person>("form", this.getModel()) {
            @Override
            public void onSubmit() {
                super.onSubmit();
                RegisterPage.this.personService.save(RegisterPage.this.getModelObject());
                this.setResponsePage(new LoginPage());
            }
        };

        FormComponentBuilderFactory.textField()
                .usingDefaults()
                .required()
                .body(new ResourceModel("username"))
                .attach(form, "username", LambdaModel.of(this.getModel(), Person::getUsername, Person::setUsername));

        FormComponentBuilderFactory.password()
                .usingDefaults()
                .required()
                .body(new ResourceModel("password"))
                .attach(form, "password", LambdaModel.of(this.getModel(), Person::getPassword, Person::setPassword));

        form.add(new SubmitLink("save"));

        this.add(form);
    }
}
