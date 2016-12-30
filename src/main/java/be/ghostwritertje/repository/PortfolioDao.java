package be.ghostwritertje.repository;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.budgetting.BankAccount;
import be.ghostwritertje.domain.investing.Portfolio;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 28-Dec-16.
 */
public interface PortfolioDao extends CrudRepository<Portfolio, String> {

    List<BankAccount> findByPerson(Person owner);

}
