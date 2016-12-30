package be.ghostwritertje.repository;

import be.ghostwritertje.domain.investing.FinancialInstrument;
import be.ghostwritertje.domain.investing.HistoricPrice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 15-Oct-16.
 */
@Repository
public interface HistoricPriceDao extends CrudRepository<HistoricPrice, String> {
    List<HistoricPrice> findByFinancialInstrument(FinancialInstrument financialInstrument);
}
