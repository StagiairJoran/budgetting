package be.ghostwritertje.services.car;

import be.ghostwritertje.domain.car.Car;
import be.ghostwritertje.domain.car.Refueling;
import be.ghostwritertje.repository.RefuelingDao;
import be.ghostwritertje.services.DomainObjectCrudServiceSupport;
import be.ghostwritertje.utilities.DateUtilities;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.time.temporal.ChronoUnit;
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
        return this.dao.findByCarOrderByDateAsc(car);
    }

    @Override
    public List<RefuelingSearchResult> mapRefuelingsToSearchResults(List<Refueling> refuelings) {
        return StreamEx.of(refuelings.stream())
                .pairMap((refueling, refueling2) -> new RefuelingSearchResult(refueling2)
                        .setKilometresPerMonth((refueling2.getKilometres()-refueling.getKilometres())/ refueling.getDate().until(refueling2.getDate(), ChronoUnit.DAYS)*30)
                        .setConsumption(refueling2.getLiters() / (refueling2.getKilometres() - refueling.getKilometres())*100))
                .collect(Collectors.toList());
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
    protected CrudRepository<Refueling, Integer> getDao() {
        return this.dao;
    }
}
