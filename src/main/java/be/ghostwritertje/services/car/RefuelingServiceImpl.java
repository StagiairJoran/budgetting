package be.ghostwritertje.services.car;

import be.ghostwritertje.domain.car.Car;
import be.ghostwritertje.domain.car.Refueling;
import be.ghostwritertje.repository.RefuelingDao;
import be.ghostwritertje.services.DomainObjectCrudServiceSupport;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Service
public class RefuelingServiceImpl extends DomainObjectCrudServiceSupport<Refueling> implements RefuelingService {

    private final RefuelingDao dao;

    @Autowired
    public RefuelingServiceImpl(RefuelingDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Refueling> findByCar(Car car) {
        return this.dao.findByCarOrderByDateDesc(car);
    }

    @Override
    public List<RefuelingSearchResult> mapRefuelingsToSearchResults(List<Refueling> refuelings) {
        List<RefuelingSearchResult> searchResults = StreamEx.of(refuelings.stream().sorted(Comparator.comparing(Refueling::getDate)))
                .pairMap((refueling, refueling2) -> new RefuelingSearchResult(refueling2)
                        //TODO error when 2 refuelings on same day (divide by zero)
                        .setKilometresPerMonth((refueling2.getKilometres() - refueling.getKilometres()) / refueling.getDate().until(refueling2.getDate(), ChronoUnit.DAYS) * 365.25 / 12)
                        .setConsumption(refueling2.getLiters() / (refueling2.getKilometres() - refueling.getKilometres()) * 100))
                .collect(Collectors.toList());
        this.averageOutPartialRefuelings(searchResults);
        return searchResults;
    }

    private void averageOutPartialRefuelings(List<RefuelingSearchResult> searchResults) {
        final List<RefuelingSearchResult> incompleteRefuelings = new ArrayList<>();

        for (RefuelingSearchResult searchResult : searchResults) {
                if (!searchResult.getRefueling().isFuelTankFull()) {
                    incompleteRefuelings.add(searchResult);
                } else {
                    incompleteRefuelings.add(searchResult);
                    BigDecimal averageConsumption =  BigDecimal.valueOf(incompleteRefuelings.stream()
                            .map(RefuelingSearchResult::getConsumption)
                            .mapToDouble(Double::doubleValue)
                            .sum())
                            .divide(BigDecimal.valueOf(incompleteRefuelings.size()), RoundingMode.HALF_DOWN);
                    incompleteRefuelings.forEach((sr) -> sr.setConsumption(averageConsumption.doubleValue()));

                    BigDecimal averageKilometres =  BigDecimal.valueOf(incompleteRefuelings.stream()
                            .map(RefuelingSearchResult::getKilometresPerMonth)
                            .mapToDouble(Double::doubleValue)
                            .sum())
                            .divide(BigDecimal.valueOf(incompleteRefuelings.size()), RoundingMode.HALF_DOWN);
                    incompleteRefuelings.forEach((sr) -> sr.setKilometresPerMonth(averageKilometres.doubleValue()));
                    incompleteRefuelings.clear();
                }
            }
    }

    @Override
    public Refueling save(Refueling refueling) {
        return this.dao.save(refueling);
    }

    @Override
    public void delete(Refueling refueling) {
        this.dao.delete(refueling);
    }

    @Override
    protected CrudRepository<Refueling, String> getDao() {
        return this.dao;
    }
}
