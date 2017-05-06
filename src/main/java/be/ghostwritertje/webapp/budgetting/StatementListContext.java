package be.ghostwritertje.webapp.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.Statement;
import be.ghostwritertje.services.budgetting.StatementService;
import be.ghostwritertje.webapp.model.LoadableListModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Jorandeboever
 * Date: 01-May-17.
 */
public class StatementListContext implements Serializable {
    private static final long serialVersionUID = 2334642848473698328L;

    private final IModel<List<Statement>> statementListModel;
    private final IModel<List<Statement>> filteredStatementListModel;
    private final IModel<StatementCriteria> statementCriteriaIModel = new Model<>(new StatementCriteria());

    private final IModel<Person> personModel;

    public StatementListContext(IModel<Person> personModel) {
        this.personModel = personModel;
        this.statementListModel = new StatementListModel(this.personModel);
        this.filteredStatementListModel = new FilteredStatementListModel(this.statementCriteriaIModel, this.statementListModel);
    }

    public IModel<List<Statement>> getStatementListModel() {
        return this.statementListModel;
    }

    public IModel<Person> getPersonModel() {
        return this.personModel;
    }

    public IModel<List<Statement>> getFilteredStatementListModel() {
        return this.filteredStatementListModel;
    }

    public IModel<StatementCriteria> getStatementCriteriaIModel() {
        return this.statementCriteriaIModel;
    }

    private final class FilteredStatementListModel extends ListModel<Statement> {
        private static final long serialVersionUID = 6182710252359471798L;

        private final IModel<StatementCriteria> criteriaIModel;
        private final IModel<List<Statement>> statementListModel;

        private FilteredStatementListModel(IModel<StatementCriteria> criteriaIModel, IModel<List<Statement>> statementListModel) {
            this.criteriaIModel = criteriaIModel;
            this.statementListModel = statementListModel;
        }

        @Override
        public List<Statement> getObject() {
            StatementCriteria criteria = this.criteriaIModel.getObject();

            return this.statementListModel.getObject().stream()
                    .filter(statement -> Optional.ofNullable(criteria.getCategory()).map(category -> Objects.equals(statement.getCategory(), category)).orElse(true))
                    .filter(statement -> Optional.ofNullable(criteria.getOriginatingAccount()).map(account -> Objects.equals(statement.getOriginatingAccount(), account)).orElse(true))
                    .filter(statement -> Optional.ofNullable(criteria.getDescription()).map(category ->  statement.getDescription().contains(category)).orElse(true))
                    .collect(Collectors.toList());
        }
    }

    private final class StatementListModel extends LoadableListModel<Statement> {
        private static final long serialVersionUID = 6182710252359471798L;
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
