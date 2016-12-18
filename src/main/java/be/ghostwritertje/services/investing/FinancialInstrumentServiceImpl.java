package be.ghostwritertje.services.investing;

import be.ghostwritertje.domain.investing.FinancialInstrument;
import be.ghostwritertje.repository.FinancialInstrumentDao;
import be.ghostwritertje.services.DomainObjectCrudServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 15-Oct-16.
 */
@Service
public class FinancialInstrumentServiceImpl extends DomainObjectCrudServiceSupport<FinancialInstrument> implements FinancialInstrumentService {

    @Autowired
    private FinancialInstrumentDao financialInstrumentDao;

    @Autowired
    private FinanceService financeService;

    @Override
    protected CrudRepository<FinancialInstrument, Integer> getDao() {
        return this.financialInstrumentDao;
    }

    @Override
    public FinancialInstrument save(FinancialInstrument object) {
        if(this.financeService.exists(object.getQuote())) {
            if(this.financialInstrumentDao.findByQuote(object.getQuote()) == null){
                return super.save(object);
            }
        }
        return null;
    }

    public List<FinancialInstrument> findFinancialInstrumentsWithoutHistory(){
        return this.financialInstrumentDao.findFinancialInstrumentsWithoutHistory();
    }

}
