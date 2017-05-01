package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.Statement;
import be.ghostwritertje.services.budgetting.StatementService;
import be.ghostwritertje.webapp.model.LoadableListModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 01-May-17.
 */
public class StatementListContext implements Serializable {
    private static final long serialVersionUID = 2334642848473698328L;

    private final IModel<List<Statement>> statementListModel;
    private final IModel<Person> personModel;

    public StatementListContext(IModel<Person> personModel) {
        this.personModel = personModel;
        this.statementListModel = new StatementListModel(this.personModel);
    }

    public IModel<List<Statement>> getStatementListModel() {
        return this.statementListModel;
    }

    public IModel<Person> getPersonModel() {
        return this.personModel;
    }

    private final class StatementListModel extends LoadableListModel<Statement> {
        @SpringBean
        private StatementService statementService;

        private final IModel<Person> personIModel;

        private StatementListModel(IModel<Person> personIModel) {
            this.personIModel = personIModel;
        }

        @Override
        protected List<Statement> load() {
            return this.statementService.findByAdministrator(this.personIModel.getObject());
        }
    }
}
