package be.ghostwritertje.repository.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Jorandeboever
 * Date: 22-Dec-16.
 */
@Import({Flyway.class})
@Configuration
public class FlywayMigrate {
    @Bean
    @Autowired
    public Flyway flywayMigrate(@Qualifier("flyway") Flyway flyway){
        flyway.repair();
        flyway.migrate();
        return flyway;
    }
}
