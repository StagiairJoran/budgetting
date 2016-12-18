package be.ghostwritertje.repository.configuration.datasource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Jorandeboever
 * Date: 01-Oct-16.
 */
@Profile("openshift")
@Configuration
public class OpenshiftDataSource {

    @Bean
    public DataSource dataSource(
            @Value("${OPENSHIFT_POSTGRESQL_DB_HOST}") String host,
            @Value("${OPENSHIFT_POSTGRESQL_DB_PORT}") String port,
            @Value("${OPENSHIFT_POSTGRESQL_DB_USERNAME}") String username,
            @Value("${OPENSHIFT_POSTGRESQL_DB_PASSWORD}") String password
    ) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://" + host + ":" + port + "/saving" + "?user=" + username + "&password=" + password);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        System.out.println("Using openshift DataSource");
        return dataSource;
    }


    @Bean
    public Properties jpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("connection.pool_size", "1");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL94Dialect");
        properties.setProperty("hibernate.current_session_context_class", "org.hibernate.context.internal.ThreadLocalSessionContext");
        properties.setProperty("hibernate.show_sql", "false");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        return properties;
    }
}
