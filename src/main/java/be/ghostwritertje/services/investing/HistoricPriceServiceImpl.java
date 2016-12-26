package be.ghostwritertje.services.investing;

import be.ghostwritertje.domain.investing.FinancialInstrument;
import be.ghostwritertje.domain.investing.HistoricPrice;
import be.ghostwritertje.repository.HistoricPriceDao;
import be.ghostwritertje.services.DomainObjectCrudServiceSupport;
import be.ghostwritertje.webapp.person.pages.LoginPage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Created by Jorandeboever
 * Date: 15-Oct-16.
 */
@Service
public class HistoricPriceServiceImpl extends DomainObjectCrudServiceSupport<HistoricPrice> implements HistoricPriceService {
    private static final Logger logger = Logger.getLogger(LoginPage.class);

    private final HistoricPriceDao historicPriceDao;
    private final FinanceService financeService;
    private final FinancialInstrumentService financialInstrumentService;

    @Autowired
    public HistoricPriceServiceImpl(HistoricPriceDao historicPriceDao, FinanceService financeService, FinancialInstrumentService financialInstrumentService) {
        this.historicPriceDao = historicPriceDao;
        this.financeService = financeService;
        this.financialInstrumentService = financialInstrumentService;
    }

    @Override
    protected CrudRepository<HistoricPrice, Integer> getDao() {
        return this.historicPriceDao;
    }

    public void initHistoricPricesForStock(FinancialInstrument financialInstrument) {
        this.save(this.financeService.createHistoricPrices(financialInstrument));
    }

    public void initMissingHistoricPrices(){
        this.financialInstrumentService.findFinancialInstrumentsWithoutHistory().forEach(this::initHistoricPricesForStock);
    }

    @Scheduled(cron = "0 0 5 ? * TUE-SAT")
    public void updateHistoricPrices(){
        logger.debug("Updating historic prices");
        this.financialInstrumentService.findAll().forEach(financialInstrument ->  this.save(this.financeService.createHistoricPrices(financialInstrument, LocalDate.now().minusDays(1))));
    }

}
