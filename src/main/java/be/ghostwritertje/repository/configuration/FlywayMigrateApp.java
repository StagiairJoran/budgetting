package be.ghostwritertje.repository.configuration;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Jorandeboever
 * Date: 22-Dec-16.
 */
@Import({FlywayMigrate.class})
@Configuration
public class FlywayMigrateApp {
    @SuppressWarnings("EmptyTryBlock")
    public static void main(String[] args) {
        try (ConfigurableApplicationContext ignored = new AnnotationConfigApplicationContext(FlywayMigrateApp.class)){

        }
    }
}
