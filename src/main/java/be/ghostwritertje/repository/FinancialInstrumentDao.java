package be.ghostwritertje.repository;

import be.ghostwritertje.domain.investing.FinancialInstrument;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 15-Oct-16.
 */
@Repository
public interface FinancialInstrumentDao extends CrudRepository<FinancialInstrument, String> {
    FinancialInstrument findByQuote(String quote);

    @Query(value = "SELECT * FROM T_FINANCIAL_INSTRUMENT a LEFT JOIN t_historic_price b ON b.financial_instrument_id = a.id WHERE b.id is NULL", nativeQuery = true)
    List<FinancialInstrument> findFinancialInstrumentsWithoutHistory();
}
