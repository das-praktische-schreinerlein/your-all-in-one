package de.yaio.app;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

/**
 * userservice-websecurity-config
 *
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger logger = Logger.getLogger(WebSecurityConfig.class);
    
    public static String CONST_FILELOCATION_APIUSERS="yaio.security.apiusers.filelocation";
    public static String CONST_FILELOCATION_ADMINUSERS="yaio.security.adminusers.filelocation";
    
    /**
     * configure Admin-Configuration
     */
    @EnableWebSecurity
    @Configuration
    @Order(1)
    public static class AdminWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            Properties users = Configurator.readProperties(System.getProperty(CONST_FILELOCATION_ADMINUSERS));
            InMemoryUserDetailsManager im = new InMemoryUserDetailsManager(users);
            auth.userDetailsService(im);
        }
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                // authorize Requests
                .authorizeRequests()
                    // grant access to admin
                    .antMatchers("/admin/**", "/manage/**")
                        .authenticated()
            .and()
                // httpBasic-authentification
                .httpBasic()
             .and()
               // disable csrf-protection
               .csrf().disable()
               // allow include as Frame for sameorigin
               .headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
               ;
            ;
        }
    }    

    /**
     * configure Static-Configuration
     */
    @EnableWebSecurity
    @Configuration
    @Order(2)
    public static class StaticWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                // authorize Requests
                .authorizeRequests()
                    // free access to static data
                    .antMatchers("/js/**", "/css/**", "/yaio-explorerapp/**", "/examples/**", "/tests/**")
                        .permitAll()
                 .and()
                    .anonymous()
                 .and()
                    // httpBasic-authentification
                    .httpBasic()
                 .and()
                   // disable csrf-protection
                   .csrf().disable()
                   // allow include as Frame for sameorigin
                   .headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
                   ;
            ;
        }
    }    

    /**
     * configure API-Configuration
     */
    @EnableWebSecurity
    @Configuration
    @Order(3)
    public static class APIWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            Properties users = Configurator.readProperties(System.getProperty(CONST_FILELOCATION_APIUSERS));
            InMemoryUserDetailsManager im = new InMemoryUserDetailsManager(users);
            auth.userDetailsService(im);
        }
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                // authorize Requests
                .authorizeRequests()
                    // grant access to api
                    .antMatchers("/nodes/**", "/exports/**", "/imports/**")
                        .permitAll()
                 .and()
                    .anonymous()
                 .and()
                    // httpBasic-authentification
                    .httpBasic()
                 .and()
                   // disable csrf-protection
                   .csrf().disable()
                   // allow include as Frame for sameorigin
                   .headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
            ;
        }
    }    

    /**
     * configure Default-Configuration
     */
    @EnableWebSecurity
    @Configuration
    @Order(4)
    public static class DefaultWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                // authorize Requests
                .authorizeRequests()
                    // secure all other
                    .anyRequest()
//                        .authenticated()
                        .permitAll()
                 .and()
                    .anonymous()
                 .and()
                    // httpBasic-authentification
                    .httpBasic()
                 .and()
                   // disable csrf-protection
                   .csrf().disable()
                   // allow include as Frame for sameorigin
                   .headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
                   ;
        }
    }    

}
