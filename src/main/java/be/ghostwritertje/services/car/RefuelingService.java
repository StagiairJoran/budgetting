package be.ghostwritertje.services.car;

import be.ghostwritertje.domain.car.Car;
import be.ghostwritertje.domain.car.Refueling;
import be.ghostwritertje.services.DomainObjectCrudService;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
public interface RefuelingService extends DomainObjectCrudService<Refueling> {

    List<Refueling> findByCar(Car car);

    RefuelingSearchResult mapRefuelingToSearchResult(Refueling refueling);

    List<RefuelingSearchResult> mapRefuelingsToSearchResults(List<Refueling> refuelings);

    Refueling save(Refueling refueling);

    void delete(Refueling refueling);
}
