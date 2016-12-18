package be.ghostwritertje.webapp.person;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.services.person.PersonService;
import be.ghostwritertje.webapp.model.LoadableModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public class PersonModel extends LoadableModel<Person> {

    @SpringBean
    private PersonService personService;

    private final IModel<Integer> idModel;

    public PersonModel(IModel<Integer> idModel) {
        super();
        this.idModel = idModel;
    }

    @Override
    protected Person load() {
        return this.personService.findOne(this.idModel.getObject());
    }
}
