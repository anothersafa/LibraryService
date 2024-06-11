package com.mylibrary.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.mylibrary.repositories.lmsDs", entityManagerFactoryRef = "lmsEntityManagerFactory", transactionManagerRef = "lmsTransactionManager")
@EnableTransactionManagement
public class LMSJpaConfig {

    private static final String[] ENTITYMANAGER_PACKAGES_TO_SCAN = {
            "com.mylibrary.entity.**"
    };

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Value("${spring.jpa.show-sql}")
    private String showSql;

    @Value("${spring.jpa.properties.hibernate.id.new_generator_mappings}")
    private String newGeneratorMappings;

    @Value("${spring.jpa.properties.hibernate.format_sql}")
    private String formatSql;

    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String jpaDialect;

    @Autowired
    @Qualifier("lms")
    DataSource lmsDs;

    private HibernateJpaVendorAdapter vendorAdaptor() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        return vendorAdapter;
    }

    private Properties addProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
        properties.setProperty("hibernate.dialect", jpaDialect);
        properties.setProperty("hibernate.show_sql", showSql);
        properties.setProperty("hibernate.format_sql", formatSql);

        return properties;
    }

    @Primary
    @Bean(name = "lmsEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean LMSEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
        emfb.setJpaVendorAdapter(vendorAdaptor());
        emfb.setPersistenceUnitName("LIBRARY-SYSTEM");
        emfb.setDataSource(lmsDs);
        emfb.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        emfb.setPackagesToScan(ENTITYMANAGER_PACKAGES_TO_SCAN);
        emfb.setJpaProperties(addProperties());

        return emfb;
    }

    @Primary
    @Bean(name = "lmsTransactionManager")
    public JpaTransactionManager lmsTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(LMSEntityManagerFactory().getObject());

        return transactionManager;
    }

}
