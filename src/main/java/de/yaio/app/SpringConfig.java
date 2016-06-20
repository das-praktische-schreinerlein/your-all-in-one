package de.yaio.app;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.aspectj.AnnotationTransactionAspect;

import javax.sql.DataSource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration  // <context:spring-configured/>
@EnableSpringConfigured // <context:spring-configured/>
@ComponentScan(basePackages = {"de.schreiner"},
        excludeFilters={@ComponentScan.Filter(type=FilterType.ANNOTATION, value=org.springframework.stereotype.Controller.class),
                @ComponentScan.Filter(type=FilterType.REGEX, pattern={".*_Roo_.*"})}
)
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@EnableAutoConfiguration
public class SpringConfig {
    //${jdbc.driverClassName}
    @Value("${spring.datasource.driver-class-name}")     private String driverClassName;
    @Value("${spring.datasource.url}")                 private String url;
    @Value("${spring.datasource.username}")             private String username;
    @Value("${spring.datasource.password}")             private String password;

    @Value("${hibernate.dialect}")         private String hibernateDialect;
    @Value("${hibernate.show_sql}")     private String hibernateShowSql;
    @Value("${hibernate.hbm2ddl.auto}") private String hibernateHbm2ddlAuto;
    @Value("${hibernate.ejb.naming_strategy}") private String hibernateEjbNamingSstrategy;

    // http://shengwangi.blogspot.de/2016/03/replace-jpa-persistencexml-with-java-config.html
        @Bean
        public BasicDataSource dataSource() {
            // org.apache.commons.dbcp.BasicDataSource
            BasicDataSource basicDataSource = new BasicDataSource();
            basicDataSource.setDriverClassName(driverClassName);
            basicDataSource.setUrl(url);
            basicDataSource.setUsername(username);
            basicDataSource.setPassword(password);
            return basicDataSource;
        }

        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
            LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
//    entityManagerFactory.setPersistenceUnitName("hibernate-persistence");
            entityManagerFactory.setDataSource(dataSource);
            entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
            entityManagerFactory.setJpaDialect(new HibernateJpaDialect());
            entityManagerFactory.setPackagesToScan("de.yaio");

            entityManagerFactory.setJpaPropertyMap(hibernateJpaProperties());
            return entityManagerFactory;
        }

        private Map<String, ?> hibernateJpaProperties() {
            HashMap<String, String> properties = new HashMap<>();
            properties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
            properties.put("hibernate.show_sql", hibernateShowSql);
            properties.put("hibernate.format_sql", "false");
            properties.put("hibernate.ejb.naming_strategy", hibernateEjbNamingSstrategy);

            properties.put("hibernate.c3p0.min_size", "2");
            properties.put("hibernate.c3p0.max_size", "5");
            properties.put("hibernate.c3p0.timeout", "300"); // 5mins
            properties.put("hibernate.dialect", hibernateDialect);

            return properties;
        }

        @Bean
        public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
            //org.springframework.orm.jpa.JpaTransactionManager
            JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
            jpaTransactionManager.setEntityManagerFactory(emf);
            return jpaTransactionManager;
        }

    // http://stackoverflow.com/questions/3573188/spring-transactional-is-applied-both-as-a-dynamic-jdk-proxy-and-an-aspectj-aspe
/**
    @Bean
    public AnnotationTransactionAspect annotationTransactionAspect() {

        AnnotationTransactionAspect bean = AnnotationTransactionAspect.aspectOf();
        bean.setTransactionManager(transactionManager(entityManagerFactory().getObject()));
        return bean;
    }
    **/
}