package be.ghostwritertje.repository.configuration.datasource;

import org.apache.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.dialect.MySQLDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Jorandeboever
 * Date: 08-Oct-16.
 */

@Configuration
public class MySqlDataSource {
    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger();
    @Bean
    @Profile("local")
    public DataSource localDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/saving");
        dataSource.setUsername("root");
        dataSource.setPassword("iiii");

        LOG.info("Using mysql local DataSource");
        return dataSource;
    }

    @Bean
    @Profile("openshift-old")
    public DataSource oldOpenshiftDataSource(
            @Value("${OPENSHIFT_MYSQL_DB_HOST}") String host,
            @Value("${OPENSHIFT_MYSQL_DB_PORT}") String port,
            @Value("${OPENSHIFT_MYSQL_DB_USERNAME}") String username,
            @Value("${OPENSHIFT_MYSQL_DB_PASSWORD}") String password,
            @Value("${OPENSHIFT_APP_NAME}") String appname

    ) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://" + host + ":" + port + "/" + appname + "?user=" + username + "&password=" + password + "&serverTimezone=UTC");
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        LOG.info("Using mysql-openshift-old DataSource");
        return dataSource;
    }

    @Bean
    @Profile("openshift")
    public DataSource openshiftDataSource(
            @Value("${MYSQL_SERVICE_HOST}") String host,
            @Value("${MYSQL_SERVICE_PORT}") String port,
            @Value("${MYSQL_USER}") String username,
            @Value("${MYSQL_PASSWORD}") String password,
            @Value("${MYSQL_DATABASE}") String appname

    ) {
        LOG.info("MYSQL_PASSWORD" + password);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://" + host + ":" + port + "/" + appname + "?user=" + username + "&password=" + password;
        LOG.info(() -> String.format("Url= %s", url));
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        LOG.info("Using mysql-openshift DataSource");
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
//        properties.setProperty("hibernate.generate_statistics", "true");
//        properties.setProperty("hibernate.hbm2ddl.auto", "update");

        LOG.info("DataSource location : " + properties.getProperty("hibernate.connection.url"));
        return properties;
    }
}
