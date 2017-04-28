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
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Service
public class RefuelingServiceImpl extends DomainObjectCrudServiceSupport<Refueling> implements RefuelingService {

    private static final double DAYS_PER_YEAR = 365.25;
    private static final double AVG_DAYS_PER_MONTH = DAYS_PER_YEAR / 12;
    private static final int SCALE_ON_DIVIDE = 10;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_DOWN;

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
                .pairMap((refueling, refueling2) -> {
                    BigDecimal kilometresDriven = refueling2.getKilometres().subtract(refueling.getKilometres());
                    long numberOfDays = refueling.getDate()
                            .until(refueling2.getDate(), ChronoUnit.DAYS);

                    BigDecimal averageKilometresDiverPerMonth = this.getAverageDistanceDrivenPerMonth(kilometresDriven, numberOfDays);

                    BigDecimal consumption = this.getAverageConsumption(kilometresDriven, refueling2.getLiters());

                    return new RefuelingSearchResult(refueling2)
                            //TODO error when 2 refuelings on same day (divide by zero)
                            .setTotalDistanceDriven(kilometresDriven)
                            .setNumberOfDays(numberOfDays)
                            .setKilometresPerMonth(averageKilometresDiverPerMonth)
                            .setConsumption(consumption);
                })
                .collect(Collectors.toList());
        return  this.averageOutPartialRefuelings(searchResults);
    }

    private BigDecimal getAverageConsumption(BigDecimal distanceDriven, BigDecimal liters) {
        return liters.divide(distanceDriven, SCALE_ON_DIVIDE, ROUNDING_MODE).multiply(BigDecimal.valueOf(100));
    }

    private BigDecimal getAverageDistanceDrivenPerMonth(BigDecimal kilometresDriven, long numberOfDays) {
        BigDecimal averageKilometresDrivenPerDay = kilometresDriven.divide(BigDecimal.valueOf(numberOfDays), SCALE_ON_DIVIDE, ROUNDING_MODE);
        return averageKilometresDrivenPerDay.multiply(BigDecimal.valueOf(AVG_DAYS_PER_MONTH));
    }

    private List<RefuelingSearchResult> averageOutPartialRefuelings(List<RefuelingSearchResult> searchResults) {
        Collection<RefuelingSearchResult> incompleteRefuelings = new ArrayList<>();

        for (RefuelingSearchResult searchResult : searchResults) {
                if (!searchResult.getRefueling().isFuelTankFull()) {
                    incompleteRefuelings.add(searchResult);
                } else {
                    incompleteRefuelings.add(searchResult);

                    BigDecimal totalLiters = incompleteRefuelings.stream()
                            .map(RefuelingSearchResult::getRefueling)
                            .map(Refueling::getLiters)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    RefuelingSearchResult first = incompleteRefuelings.stream()
                            .findFirst()
                            .orElse(null);

                    Refueling last = incompleteRefuelings.stream()
                            .map(RefuelingSearchResult::getRefueling)
                            .reduce((refueling, refueling2) -> refueling2)
                            .orElse(null);

                    long numberOfDays = first.getRefueling().getDate().until(last.getDate(), ChronoUnit.DAYS) + first.getNumberOfDays();

                    BigDecimal totalDistanceDriven = last.getKilometres().subtract(first.getRefueling().getKilometres()).add(first.getTotalDistanceDriven());
                    BigDecimal averageConsumption = this.getAverageConsumption(totalDistanceDriven, totalLiters);
                    BigDecimal averageDistanceDrivenPerMonth = this.getAverageDistanceDrivenPerMonth(totalDistanceDriven, numberOfDays);

                    incompleteRefuelings.forEach((sr) -> {
                        sr.setKilometresPerMonth(averageDistanceDrivenPerMonth);
                        sr.setConsumption(averageConsumption);
                    });

                    incompleteRefuelings.clear();
                }
            }

            return searchResults;
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
