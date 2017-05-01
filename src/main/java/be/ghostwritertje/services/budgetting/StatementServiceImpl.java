package be.ghostwritertje.services.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.BankAccount;
import be.ghostwritertje.domain.budgetting.Category;
import be.ghostwritertje.domain.budgetting.Statement;
import be.ghostwritertje.repository.StatementDao;
import be.ghostwritertje.services.DomainObjectCrudServiceSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Service
public class StatementServiceImpl extends DomainObjectCrudServiceSupport<Statement> implements StatementService {
    private final StatementDao dao;
    private final BankAccountService bankAccountService;
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    public StatementServiceImpl(StatementDao dao, BankAccountService bankAccountService) {
        this.dao = dao;
        this.bankAccountService = bankAccountService;
    }

    @Override
    protected CrudRepository<Statement, String> getDao() {
        return this.dao;
    }

    @Override
    public Long findNumberOfStatementsForCategory(Category category, Person administrator) {
        return this.dao.findNumberOfStatementsForCategory(category, administrator);
    }

    @Override
    public Long findNumberOfStatementsWithoutCategory(Person administrator) {
        return this.dao.findNumberOfStatementsWithoutCategory(administrator);
    }

    @Override
    public Iterable<Statement> save(Iterable<Statement> statements) {
        return this.dao.save(statements);
    }

    @Override
    public List<Statement> findByAdministrator(Person administrator) {
//        List<Statement> statements = new ArrayList<>();
//        this.bankAccountService.findByAdministrator(administrator).forEach(bankAccount -> statements.addAll(this.dao.findByOriginatingAccount(bankAccount)));
//        return statements;
        return this.dao.findByOriginatingAccount_Administrator(administrator);
    }

    @Override
    public List<Statement> findByOriginatingAccount(BankAccount bankAccount) {
        return this.dao.findByOriginatingAccount(bankAccount);
    }

    @Override
    public BigDecimal getTotal(BankAccount bankAccount) {
        return this.dao.findByOriginatingAccount(bankAccount).stream().map(Statement::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotal(Person owner) {
        return this.bankAccountService.findByOwner(owner).stream().map(this::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BankAccountsHistoricData getBankAccountsHistoricDataForPersonByMonth(Person person) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("getBankAccountsHistoricDataForPersonByMonth");

        List<Statement> statements = this.findByAdministrator(person);

//        LocalDate first = statements.stream()
//                .map(Statement::getDate)
//                .sorted()
//                .findFirst()
//                .orElseGet(LocalDate::now);

        LocalDate first = LocalDate.of(2016,1,1);

        LocalDate dateToMap = LocalDate.of(first.getYear(), first.getMonth(), first.lengthOfMonth());


        List<BankAccount> bankAccountsByOwner = this.bankAccountService.findByOwner(person);
        Map<BankAccount, List<Statement>> map = bankAccountsByOwner.stream().collect(Collectors.toMap(o -> o, o -> new ArrayList<>()));
        Map<BankAccount, List<Number>> theMap = bankAccountsByOwner.stream().collect(Collectors.toMap(o -> o, o -> new ArrayList<>()));

        map.forEach((bankAccount, statementsForBankAccount) -> statementsForBankAccount.addAll(statements.stream()
                .filter(statement -> statement.getOriginatingAccount().getUuid().equals(bankAccount.getUuid()))
                .collect(Collectors.toList())));


        Collection<LocalDate> datesToMap = new ArrayList<>();

        while (dateToMap.isBefore(LocalDate.now())) {
            datesToMap.add(dateToMap);

            LocalDate newDate = LocalDate.from(dateToMap);

            theMap.forEach((bankAccount, numbers) -> {
                numbers.add(map.get(bankAccount).stream()
                        .filter(statement -> statement.getDate().isBefore(newDate))
                        .map(Statement::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
            });

            dateToMap = dateToMap.plusMonths(1);
        }

        List<String> dateStrings = datesToMap.stream().map(date -> String.format("%s %s", date.getMonth(), date.getYear())).collect(Collectors.toList());

        stopWatch.stop();
        LOG.info(stopWatch.prettyPrint());
        System.out.println(stopWatch.prettyPrint());
        return new BankAccountsHistoricData(dateStrings, theMap);
    }

}
