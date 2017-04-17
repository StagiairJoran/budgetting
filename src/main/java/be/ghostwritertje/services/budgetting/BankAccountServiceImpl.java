package be.ghostwritertje.services.budgetting;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.BankAccount;
import be.ghostwritertje.repository.BankAccountDao;
import be.ghostwritertje.services.DomainObjectCrudServiceSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Service
public class BankAccountServiceImpl extends DomainObjectCrudServiceSupport<BankAccount> implements BankAccountService {
    private final BankAccountDao dao;

    private static final Logger LOG = LogManager.getLogger();


    @Autowired
    public BankAccountServiceImpl(BankAccountDao dao) {
        this.dao = dao;
    }

    @Override
    protected CrudRepository<BankAccount, String> getDao() {
        return this.dao;
    }

    @Override
    public List<BankAccount> findByAdministrator(Person administrator) {
        return this.dao.findByAdministrator(administrator);
    }

    @Override
    public List<BankAccount> findByOwner(Person owner) {
        return this.dao.findByOwner(owner);
    }


    @Override
    public Iterable<BankAccount> save(Iterable<BankAccount> bankAccounts) {
        bankAccounts.forEach(bankAccount -> {
            LOG.warn("Bankaccount with UUID: %S | name: %s | number: %s | Owner: %s | Administrator: %s | Bank: %s", bankAccount.getUuid(), bankAccount.getName(), bankAccount.getNumber(), bankAccount.getOwner(), bankAccount.getAdministrator(), bankAccount.getBank());
        });

        return this.dao.save(bankAccounts);
    }
}
