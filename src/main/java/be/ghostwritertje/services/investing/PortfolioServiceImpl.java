package be.ghostwritertje.services.investing;

import be.ghostwritertje.domain.investing.HistoricPrice;
import be.ghostwritertje.domain.investing.Portfolio;
import be.ghostwritertje.repository.PortfolioDao;
import be.ghostwritertje.services.DomainObjectCrudServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * Created by Jorandeboever
 * Date: 28-Dec-16.
 */
@Service
public class PortfolioServiceImpl extends DomainObjectCrudServiceSupport<Portfolio> implements PortfolioService {

    private final PortfolioDao dao;

    @Autowired
    public PortfolioServiceImpl(PortfolioDao dao) {
        this.dao = dao;
    }

    @Override
    protected CrudRepository<Portfolio, Integer> getDao() {
        return this.dao;
    }
}
