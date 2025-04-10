package com.utng.edu.prueba.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.env.Environment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.orm.jpa.JpaTransactionManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableTransactionManagement
@ConfigurationProperties(prefix = "datasource.ucp")
@EntityScan(basePackages = "com.utng.edu.prueba.entity.empresa")
@EnableJpaRepositories(

        entityManagerFactoryRef = "empresaEntityManagerFactory",
        transactionManagerRef = "empresaTransactionManager",
        basePackages = {"com.utng.edu.prueba.repositories.empresa"})

public class configBase {
    @Autowired
    private Environment env;

    @Bean(name = "empresaDataSource")
    public DataSource empresaDataSource() {
        log.info("Inicia pool de empresa");
        HikariDataSource empresa = new HikariDataSource();
        try {
            empresa.setJdbcUrl(env.getProperty("spring.datasource.url"));
            empresa.setUsername(env.getProperty("spring.datasource.username"));
            empresa.setPassword(env.getProperty("spring.datasource.password"));
            empresa.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
            empresa.setMaximumPoolSize(10);
            empresa.setMinimumIdle(5);
            empresa.setIdleTimeout(30000);
            empresa.setConnectionTimeout(20000);
            empresa.setMaxLifetime(1800000);
            return empresa;
        } catch (Exception ae) {
            log.error("Error en la conexión de base de datos: {}", ae.getMessage(), ae);
        }
        return null;
    }

    @Primary
    @Bean(name = "empresaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(empresaDataSource());
        em.setPackagesToScan("com.utng.edu.prueba.entity.empresa");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.query.timeout", 60000);
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean(name = "empresaTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("empresaEntityManagerFactory") LocalContainerEntityManagerFactoryBean topicsEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(topicsEntityManagerFactory.getObject()));
    }
}