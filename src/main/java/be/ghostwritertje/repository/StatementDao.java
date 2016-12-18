package be.ghostwritertje.repository;

import be.ghostwritertje.domain.budgetting.BankAccount;
import be.ghostwritertje.domain.budgetting.Statement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Repository
public interface StatementDao extends CrudRepository<Statement, Integer> {

    List<Statement> findByOriginatingAccount(BankAccount from);
}
