package be.ghostwritertje.services.car;

import be.ghostwritertje.domain.car.Car;
import be.ghostwritertje.domain.car.Refueling;
import be.ghostwritertje.repository.RefuelingDao;
import be.ghostwritertje.services.DomainObjectCrudServiceSupport;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Service
public class RefuelingServiceImpl extends DomainObjectCrudServiceSupport<Refueling> implements RefuelingService {

    @Autowired
    private RefuelingDao dao;

    @Override
    public List<Refueling> findByCar(Car car) {
        return this.dao.findByCarOrderByDateAsc(car);
    }

    @Override
    public List<RefuelingSearchResult> mapRefuelingsToSearchResults(List<Refueling> refuelings) {
        return StreamEx.of(refuelings.stream())
                .pairMap((refueling, refueling2) -> new RefuelingSearchResult(refueling2)
                        .setKilometresPerMonth(refueling2.getKilometres()-refueling.getKilometres()/ Period.between(refueling.getDate(), refueling2.getDate()).getDays()*30)
                        .setConsumption(refueling.getLiters() / (refueling2.getKilometres() - refueling.getKilometres())*100))
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
