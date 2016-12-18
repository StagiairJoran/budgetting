package be.ghostwritertje.services.investing;

import be.ghostwritertje.domain.investing.HistoricPrice;
import be.ghostwritertje.services.DomainObjectCrudService;

/**
 * Created by Jorandeboever
 * Date: 15-Oct-16.
 */
public interface HistoricPriceService extends DomainObjectCrudService<HistoricPrice> {
    void initMissingHistoricPrices();
}
