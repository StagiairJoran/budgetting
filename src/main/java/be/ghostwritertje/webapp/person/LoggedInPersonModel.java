package be.ghostwritertje.webapp.person;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.webapp.CustomSession;
import be.ghostwritertje.webapp.model.LoadableModel;

/**
 * Created by Jorandeboever
 * Date: 14-Jan-17.
 */
public class LoggedInPersonModel extends LoadableModel<Person> {

    @Override
    protected Person load() {
        return CustomSession.get().getLoggedInPerson();
    }
}
