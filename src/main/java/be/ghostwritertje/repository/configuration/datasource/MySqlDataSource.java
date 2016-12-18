package be.ghostwritertje.repository.configuration.datasource;

import org.apache.log4j.Logger;
import org.hibernate.dialect.MySQLDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Jorandeboever
 * Date: 08-Oct-16.
 */
@Profile("local")
@Configuration
public class MySqlDataSource {
    private static final Logger logger = Logger.getLogger(H2DataSource.class);

    @Bean
    public DataSource localDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/saving");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        logger.info("Using openshift DataSource");
        return dataSource;
    }

    @Bean
    public Properties jpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("connection.pool_size", "1");
        properties.setProperty("hibernate.dialect", MySQLDialect.class.getName());
        properties.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/saving");
        properties.setProperty("hibernate.current_session_context_class", "org.hibernate.context.internal.ThreadLocalSessionContext");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        logger.info("DataSource location : " + properties.getProperty("hibernate.connection.url"));
        return properties;
    }
}
