package de.yaio.app;

import de.yaio.app.config.PersistenceConfig;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.security.AuthenticationManagerConfiguration;
import org.springframework.boot.autoconfigure.security.BootGlobalAuthenticationConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.web.*;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

@Configuration  // <context:spring-configured/>
@Lazy
@EnableSpringConfigured() // <context:spring-configured/>
@ComponentScan(basePackages = {"de.yaio.app.core"},
        excludeFilters={@ComponentScan.Filter(type=FilterType.ANNOTATION, value=org.springframework.stereotype.Controller.class),
                @ComponentScan.Filter(type=FilterType.REGEX, pattern={".*_Roo_.*"})}
)
@Import({PersistenceConfig.class,
        AopAutoConfiguration.class,
        AopAutoConfiguration.JdkDynamicAutoProxyConfiguration.class,
        AuthenticationManagerConfiguration.class,
        BootGlobalAuthenticationConfiguration.class,
        DispatcherServletAutoConfiguration.class,
        EmbeddedServletContainerAutoConfiguration.class,
        EmbeddedServletContainerAutoConfiguration.EmbeddedTomcat.class,
        ErrorMvcAutoConfiguration.class,
        FlywayAutoConfiguration.class,
        HttpEncodingAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
        JacksonAutoConfiguration.class,
        MultipartAutoConfiguration.class,
        PersistenceExceptionTranslationAutoConfiguration.class,
        PropertyPlaceholderAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        SecurityFilterAutoConfiguration.class,
        ServerPropertiesAutoConfiguration.class
})
public class MinimalTestContextConfig {
}