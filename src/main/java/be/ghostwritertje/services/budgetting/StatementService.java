package be.ghostwritertje.services.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.BankAccount;
import be.ghostwritertje.domain.budgetting.Statement;
import be.ghostwritertje.services.DomainObjectCrudService;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public interface StatementService extends DomainObjectCrudService<Statement> {

    Iterable<Statement> save(Iterable<Statement> statements);

    List<Statement> findAll(Person administrator);

    BigDecimal getTotal(BankAccount bankAccount);

    BigDecimal getTotal(Person owner);
}
