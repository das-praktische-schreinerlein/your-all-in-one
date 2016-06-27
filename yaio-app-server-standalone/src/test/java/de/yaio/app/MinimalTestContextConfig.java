package de.yaio.app;

import de.yaio.app.config.PersistenceConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

@Configuration  // <context:spring-configured/>
@EnableSpringConfigured() // <context:spring-configured/>
@ComponentScan(basePackages = {"de.yaio.app.core"},
        excludeFilters={@ComponentScan.Filter(type=FilterType.ANNOTATION, value=org.springframework.stereotype.Controller.class),
                @ComponentScan.Filter(type=FilterType.REGEX, pattern={".*_Roo_.*"})}
)
@EnableAutoConfiguration
@Import(PersistenceConfig.class)
public class MinimalTestContextConfig {
}