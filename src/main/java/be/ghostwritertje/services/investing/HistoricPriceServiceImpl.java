package be.ghostwritertje.services.investing;

import be.ghostwritertje.domain.investing.FinancialInstrument;
import be.ghostwritertje.domain.investing.HistoricPrice;
import be.ghostwritertje.repository.HistoricPriceDao;
import be.ghostwritertje.services.DomainObjectCrudServiceSupport;
import be.ghostwritertje.webapp.person.pages.LoginPage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;

/**
 * Created by Jorandeboever
 * Date: 15-Oct-16.
 */
@Service
public class HistoricPriceServiceImpl extends DomainObjectCrudServiceSupport<HistoricPrice> implements HistoricPriceService {
    private static final Logger logger = Logger.getLogger(LoginPage.class);

    private final HistoricPriceDao historicPriceDao;
    private final FinanceService financeService;

    @Autowired
    public HistoricPriceServiceImpl(HistoricPriceDao historicPriceDao, FinanceService financeService) {
        this.historicPriceDao = historicPriceDao;
        this.financeService = financeService;
    }

    @Override
    protected CrudRepository<HistoricPrice, String> getDao() {
        return this.historicPriceDao;
    }

    @Override
    public void updateHistoricPrices(FinancialInstrument financialInstrument) {
        logger.debug("Updating historic prices for " + financialInstrument.getQuote());
        this.save(this.financeService.createHistoricPrices(financialInstrument, financialInstrument.getHistoricPriceList().stream().
                sorted(Comparator.comparing(HistoricPrice::getDate).reversed())
                .findFirst()
                .map(HistoricPrice::getDate)
                .orElse(LocalDate.of(1970, 1, 1)).plusDays(1)));
    }

    @Override
    public void createHistoricPrices(FinancialInstrument financialInstrument) {
        logger.debug("Creating historic prices for " + financialInstrument.getQuote());
        this.save(this.financeService.createHistoricPrices(financialInstrument));
    }

}
