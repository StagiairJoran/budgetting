package be.ghostwritertje.services.investing;

import be.ghostwritertje.domain.Person;
import be.ghostwritertje.domain.investing.FinancialInstrument;
import be.ghostwritertje.domain.investing.FundPurchase;
import be.ghostwritertje.repository.FundPurchaseDao;
import be.ghostwritertje.services.DomainObjectCrudServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 08-Oct-16.
 */
@Service
public class FundPurchaseServiceImpl extends DomainObjectCrudServiceSupport<FundPurchase> implements FundPurchaseService {
    @Autowired
    private FundPurchaseDao dao;

    @Autowired
    private FinancialInstrumentService financialInstrumentService;

    @Autowired
    private HistoricPriceService historicPriceService;

    @Override
    protected CrudRepository<FundPurchase, Integer> getDao() {
        return this.dao;
    }

    @Override
    public List<FundPurchase> findByOwner(Person owner) {
        return this.dao.findByOwner(owner);
    }

    @Override
    public FundPurchase save(FundPurchase object) {
        financialInstrumentService.save(new FinancialInstrument(object.getQuote()));
        this.historicPriceService.initMissingHistoricPrices();
        return super.save(object);
    }
}
