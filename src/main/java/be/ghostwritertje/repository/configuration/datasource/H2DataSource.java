package be.ghostwritertje.repository.configuration.datasource;

import org.apache.log4j.Logger;
import org.hibernate.dialect.H2Dialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Profile("h2")
@Configuration
public class H2DataSource {
    private static final Logger logger = Logger.getLogger(H2DataSource.class);

    @Bean
    public DataSource localDataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder
                .setType(EmbeddedDatabaseType.H2)
                .setName("h2")
                .build();
    }

    @Bean
    public Properties jpaProperties(@Value("${h2.datasource.location}") String dataSourceLocation) {
        Properties properties = new Properties();
        properties.setProperty("connection.pool_size", "1");
        properties.setProperty("hibernate.dialect", H2Dialect.class.getName());
        properties.setProperty("hibernate.connection.url", String.format("jdbc:h2:%s", dataSourceLocation));
        properties.setProperty("hibernate.current_session_context_class", "org.hibernate.context.internal.ThreadLocalSessionContext");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        logger.info("DataSource location : " + properties.getProperty("hibernate.connection.url"));
        return properties;
    }
}
