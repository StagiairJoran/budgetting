package be.ghostwritertje.services.investing;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.investing.FundPurchase;
import be.ghostwritertje.services.DomainObjectCrudService;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 08-Oct-16.
 */
public interface FundPurchaseService extends DomainObjectCrudService<FundPurchase> {
    List<FundPurchase> findByOwner(Person owner);
}
