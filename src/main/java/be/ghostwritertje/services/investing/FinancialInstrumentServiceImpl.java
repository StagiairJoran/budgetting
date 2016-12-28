package be.ghostwritertje.services.investing;

import be.ghostwritertje.domain.investing.FinancialInstrument;
import be.ghostwritertje.repository.FinancialInstrumentDao;
import be.ghostwritertje.services.DomainObjectCrudServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 15-Oct-16.
 */
@Service
public class FinancialInstrumentServiceImpl extends DomainObjectCrudServiceSupport<FinancialInstrument> implements FinancialInstrumentService {

    private final FinancialInstrumentDao financialInstrumentDao;
    private final FinanceService financeService;
    private final HistoricPriceService historicPriceService;

    @Autowired
    public FinancialInstrumentServiceImpl(FinancialInstrumentDao financialInstrumentDao, FinanceService financeService, HistoricPriceService historicPriceService) {
        this.financialInstrumentDao = financialInstrumentDao;
        this.financeService = financeService;
        this.historicPriceService = historicPriceService;
    }

    @Override
    protected FinancialInstrumentDao getDao() {
        return this.financialInstrumentDao;
    }

    @Override
    public FinancialInstrument save(FinancialInstrument object) {
        if(this.financeService.exists(object.getQuote())) {
            if(this.financialInstrumentDao.findByQuote(object.getQuote()) == null){
                FinancialInstrument save = super.save(object);
                this.createHistoricPrices(save);
                return save;
            }
        }
        return null;
    }

    public List<FinancialInstrument> findFinancialInstrumentsWithoutHistory(){
        return this.financialInstrumentDao.findFinancialInstrumentsWithoutHistory();
    }

    @Override
    public FinancialInstrument findByQuote(String quote) {
        return this.getDao().findByQuote(quote);
    }

    public void createHistoricPrices(FinancialInstrument financialInstrument) {
        this.historicPriceService.createHistoricPrices(financialInstrument);
    }

    @Scheduled(cron = "0 0 5 ? * TUE-SAT")
    public void updateHistoricPrices(){
        this.findAll().forEach(this.historicPriceService::updateHistoricPrices);
    }
}
