package be.ghostwritertje.services.investing;

import be.ghostwritertje.domain.investing.FinancialInstrument;
import be.ghostwritertje.services.DomainObjectCrudService;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 15-Oct-16.
 */
public interface FinancialInstrumentService extends DomainObjectCrudService<FinancialInstrument> {
    List<FinancialInstrument> findFinancialInstrumentsWithoutHistory();
}
