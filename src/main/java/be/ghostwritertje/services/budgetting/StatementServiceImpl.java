package be.ghostwritertje.services.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.BankAccount;
import be.ghostwritertje.domain.budgetting.Statement;
import be.ghostwritertje.repository.StatementDao;
import be.ghostwritertje.services.DomainObjectCrudServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Service
public class StatementServiceImpl extends DomainObjectCrudServiceSupport<Statement> implements StatementService {
    @Autowired
    private StatementDao dao;

    @Autowired
    private BankAccountService bankAccountService;

    @Override
    protected CrudRepository<Statement, Integer> getDao() {
        return this.dao;
    }

    @PostConstruct
    public void postConstruct() {
        this.dao.save(new Statement());
    }

    @Override
    public Iterable<Statement> save(Iterable<Statement> statements) {
        return this.dao.save(statements);
    }

    @Override
    public List<Statement> findAll(Person administrator) {
        List<Statement> statements = new ArrayList<>();
        this.bankAccountService.findByAdministrator(administrator).forEach(bankAccount -> statements.addAll(this.dao.findByOriginatingAccount(bankAccount)));
        return statements;
    }

    @Override
    public BigDecimal getTotal(BankAccount bankAccount) {
        return this.dao.findByOriginatingAccount(bankAccount).stream().map(Statement::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotal(Person owner) {
        return this.bankAccountService.findByOwner(owner).stream().map(this::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
