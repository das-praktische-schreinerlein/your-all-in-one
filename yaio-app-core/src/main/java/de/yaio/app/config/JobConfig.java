package de.yaio.app.config;

import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration  // <context:spring-configured/>
@EnableSpringConfigured // <context:spring-configured/>
@EnableJpaRepositories(basePackages = {"de.yaio.app.core"})
@ComponentScan(basePackages = {"de.yaio.app.core"},
        excludeFilters={@ComponentScan.Filter(type=FilterType.ANNOTATION, value=org.springframework.stereotype.Controller.class),
                @ComponentScan.Filter(type=FilterType.REGEX, pattern={".*_Roo_.*"})}
)
@Import({PersistenceConfig.class,
        AopAutoConfiguration.class,
        AopAutoConfiguration.JdkDynamicAutoProxyConfiguration.class,
        JacksonAutoConfiguration.class,
        PersistenceExceptionTranslationAutoConfiguration.class,
        PropertyPlaceholderAutoConfiguration.class
})
public class JobConfig {
}