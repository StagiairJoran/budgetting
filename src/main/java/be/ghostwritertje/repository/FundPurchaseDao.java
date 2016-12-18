package be.ghostwritertje.repository;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.investing.FundPurchase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 08-Oct-16.
 */
@Repository
public interface FundPurchaseDao extends CrudRepository<FundPurchase, Integer> {
    List<FundPurchase> findByOwner(Person owner);
}
