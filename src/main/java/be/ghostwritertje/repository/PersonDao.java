package be.ghostwritertje.repository;

import be.ghostwritertje.domain.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Ghostwritertje
 * Date: 29-Sep-16.
 */
@Repository
public interface PersonDao extends CrudRepository<Person, Integer> {

    Person findByUsername(String username);

    Person findByUsernameAndPassword(String username, String password);
}
