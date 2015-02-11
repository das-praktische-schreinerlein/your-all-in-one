package de.yaio.app;

import java.util.Arrays;
import java.util.List;
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
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode;
import org.springframework.security.web.util.AntPathRequestMatcher;

import de.yaio.rest.controller.CsrfHeaderFilter;

/**
 * userservice-websecurity-config
 *
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger logger = Logger.getLogger(WebSecurityConfig.class);
    
    public static String CONST_FILELOCATION_APIUSERS="yaio.security.apiusers.filelocation";
    
    /**
     * configure API-Configuration
     */
    @EnableWebSecurity
    @Configuration
    @Order(1)
    public static class APIExportsSecurityConfigurerAdapter extends APIWebSecurityConfigurerAdapter {
        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            String xframeAllowedDomains = System.getProperty("yaio.my-domain", "dummy") 
                            + "," + System.getProperty("yaio.security.xframe-allowed-domains", "");
            List<String> xframeAllowedDomainsList = Arrays.asList(xframeAllowedDomains.split(","));
            logger.info("APIExportsSecurityConfigurerAdapter xframeallowOptions:" + xframeAllowedDomainsList);
            http
                    // authentification
                    .httpBasic()
                .and()
                    .requestMatcher(new AntPathRequestMatcher("/exports/**", "GET"))
                        .authorizeRequests()
                        // secure API webservice
                        .anyRequest()
                            .authenticated()
                .and()
                   // disable csrf-protection
                   .csrf().disable()
                   .headers()
//                        .frameOptions().disable()
//                        .addHeaderWriter(
//                                   new XFrameOptionsHeaderWriter(
//                                                   new WhiteListedAllowFromStrategy(
//                                                                   xframeAllowedDomainsList)))
                           .addHeaderWriter(
                                           new XFrameOptionsHeaderWriter(
                                                           XFrameOptionsMode.SAMEORIGIN))
            ;
        }
    }    

    /**
     * configure API-Configuration
     */
    @EnableWebSecurity
    @Configuration
    @Order(2)
    public static class APIImportsSecurityConfigurerAdapter extends APIWebSecurityConfigurerAdapter {
        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            http
                    // authentification
                    .httpBasic()
                .and()
                    .requestMatcher(new AntPathRequestMatcher("/imports/**", "POST"))
                        .authorizeRequests()
                        // secure API webservice
                        .anyRequest()
                            .authenticated()
                .and()
                   // disable csrf-protection
                   .csrf().disable()
            ;
        }
    }    

    /**
     * configure API-Configuration
     */
    @EnableWebSecurity
    @Configuration
    @Order(3)
    public static class APIAdminWebSecurityConfigurerAdapter extends APIWebSecurityConfigurerAdapter {
        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            http
                    // authentification
                    .httpBasic()
                .and()
                    .requestMatcher(new AntPathRequestMatcher("/admin/**", "GET"))
                        .authorizeRequests()
                        // secure API webservice
                        .anyRequest()
                            .hasRole("ADMIN")
                .and()
                   // disable csrf-protection
                   .csrf().disable()
            ;
        }
    }    

    /**
     * configure API-Configuration
     */
    @EnableWebSecurity
    @Configuration
    @Order(4)
    public static class APIWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        private CsrfTokenRepository csrfTokenRepository() {
          HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
          repository.setHeaderName("X-XSRF-TOKEN");
          return repository;
        }        

        @Autowired
        public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
            Properties users = Configurator.readProperties(System.getProperty(CONST_FILELOCATION_APIUSERS));
            InMemoryUserDetailsManager im = new InMemoryUserDetailsManager(users);
            auth.userDetailsService(im);
        }
        
        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            String xframeAllowedDomains = System.getProperty("yaio.my-domain", "dummy") 
                            + "," + System.getProperty("yaio.security.xframe-allowed-domains", "");
            List<String> xframeAllowedDomainsList = Arrays.asList(xframeAllowedDomains.split(","));
            logger.info("APIExportsSecurityConfigurerAdapter xframeallowOptions:" + xframeAllowedDomainsList);
            http
                    // authentification
                    .formLogin()
                        .defaultSuccessUrl("/yaio-explorerapp/yaio-explorerapp.html", true)
                        // if set the defaultprocess doesnt match :-( loginPage(loginPage)
                .and() 
                    // authorize Requests
                    .authorizeRequests()
                        .antMatchers("/js/**", "/css/**", "/yaio-explorerapp/**", "/examples/**", "/tests/**", "/user/current", "/login", "/logout")
                            .permitAll()
//                        .antMatchers("/admin/**")
//                            .hasRole("ADMIN")
                        .antMatchers("/manage/**")
                            .hasRole("SUPERUSER")
                        .anyRequest()
//                            .permitAll()
                            .authenticated()
                 .and()
                     // authentification
                     .logout().permitAll()
                 .and()
                   // disable csrf-protection
                   .csrf().disable()
                   //.csrf().csrfTokenRepository(csrfTokenRepository());
                   // allow include as Frame for sameorigin
                   .headers()
//                        .frameOptions().disable()
//                         .addHeaderWriter(
//                                   new XFrameOptionsHeaderWriter(
//                                                   new WhiteListedAllowFromStrategy(
//                                                                   xframeAllowedDomainsList)))
                          .addHeaderWriter(
                                           new XFrameOptionsHeaderWriter(
                                                           XFrameOptionsMode.SAMEORIGIN))
                 .and()
                   // add CsrfHeaderFilter because angular uses another Header
                   .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);
        }
    }    
}
