package be.ghostwritertje.services;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Jorandeboever on 5/6/2017.
 */
@Service
public class FlywayServiceImpl implements FlywayService {

    private final Flyway flyway;

    @Autowired
    public FlywayServiceImpl(Flyway flyway) {
        this.flyway = flyway;
    }

    @Override
    public void reset() {
        this.flyway.clean();
        this.flyway.migrate();
    }
}
