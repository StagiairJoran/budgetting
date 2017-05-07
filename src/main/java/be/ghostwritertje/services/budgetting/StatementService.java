package be.ghostwritertje.services.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.BankAccount;
import be.ghostwritertje.domain.budgetting.Category;
import be.ghostwritertje.domain.budgetting.Statement;
import be.ghostwritertje.services.DomainObjectCrudService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public interface StatementService extends DomainObjectCrudService<Statement> {

    Iterable<Statement> save(Iterable<Statement> statements);

    List<Statement> findByAdministrator(Person administrator);

    List<Statement> findByOriginatingAccount(BankAccount bankAccount);

    Long findNumberOfStatementsForCategory(Category category, Person administrator);

    BigDecimal findSumOfStatementsByCategoryBetweenDates(Category category, Person administrator, LocalDate beginDate, LocalDate endDate);

    Long findNumberOfStatementsWithoutCategory(Person administrator);

    BigDecimal getTotal(BankAccount bankAccount);

    BigDecimal getTotal(Person owner);

    BankAccountsHistoricData getBankAccountsHistoricDataForPersonByMonth(Person person);
}
