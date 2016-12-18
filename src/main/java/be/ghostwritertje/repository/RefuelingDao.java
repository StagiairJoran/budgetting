package be.ghostwritertje.repository;

import be.ghostwritertje.domain.car.Car;
import be.ghostwritertje.domain.car.Refueling;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Repository
public interface RefuelingDao extends CrudRepository<Refueling, Integer> {

    List<Refueling> findByCar(Car car);

    void delete(Refueling refueling);
}
