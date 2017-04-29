package be.ghostwritertje.repository;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.BankAccount;
import be.ghostwritertje.domain.budgetting.Category;
import be.ghostwritertje.domain.budgetting.Statement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Repository
public interface StatementDao extends CrudRepository<Statement, String> {

    List<Statement> findByOriginatingAccount(BankAccount from);

    @Query(value = "SELECT COUNT(AMOUNT), B.NAME FROM T_STATEMENT S\n" +
            "INNER JOIN T_BANKACCOUNT B ON S.ORIGINATINGACCOUNT_UUID = B.UUID\n" +
            "WHERE CATEGORY_UUID = ?1 AND b.ADMINISTRATOR_UUID = ?2", nativeQuery = true)
    Long findNumberOfStatementsForCategory(Category category, Person administrator);
}
