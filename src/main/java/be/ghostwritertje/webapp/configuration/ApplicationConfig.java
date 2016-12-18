package be.ghostwritertje.webapp.configuration;

import be.ghostwritertje.services.ServiceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Jorandeboever
 * Date: 10-Oct-16.
 */
@Configuration
@Import(ServiceConfig.class)
public class ApplicationConfig {
}
