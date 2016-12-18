package be.ghostwritertje.webapp;

import be.ghostwritertje.domain.Person;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class CustomSession extends AuthenticatedWebSession {

    private Person loggedInPerson = null;

    public CustomSession(Request request) {
        super(request);
    }

    @Override
    public Roles getRoles() {
        return new Roles(Roles.USER);
    }

    @Override
    protected boolean authenticate(String username, String password) {
        return true;
    }

    public static CustomSession get() {
        return (CustomSession) Session.get();
    }

    public Person getLoggedInPerson() {
        return loggedInPerson;
    }

    public void setLoggedInPerson(Person loggedInPerson) {
        this.loggedInPerson = loggedInPerson;
    }
}
