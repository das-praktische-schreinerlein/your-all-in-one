package de.yaio.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
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
    public static final String CONST_FILELOCATION_APIUSERS = "yaio.security.apiusers.filelocation";
    
    private static final Logger logger = Logger.getLogger(WebSecurityConfig.class);
    
    /**
     * configure API-Configuration for Export per HttpBasic-Auth (ICal-Clients..)
     */
    @EnableWebSecurity
    @Configuration
    @Order(1)
    public static class APIExportsSecurityConfigurerAdapter extends APIWebSecurityConfigurerAdapter {
        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            http
                    // authentification
                    .httpBasic()
                .and()
                        // secure path
                        .requestMatcher(new AntPathRequestMatcher("/exports/**", "GET"))
                            .authorizeRequests()
                        // secure API webservice
                        .anyRequest()
                            .authenticated()
                .and()
                   // disable csrf-protection
                   .csrf().disable()
                   .headers()
//                        .frameOptions().disable();
//                        .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.ALLOW_FROM))
//                        .addHeaderWriter(new XFrameOptionsHeaderWriter(new WhiteListedAllowFromStrategy(WebSecurityConfig.getAllowedDomainList())));
                        // allow include as Frame for sameorigin
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN));
        }
    }    

    /**
     * configure API-Configuration for Import per HttpBasic-Auth (ICal-Clients..)
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
                    // secure path
                    .requestMatcher(new AntPathRequestMatcher("/imports/**", "POST"))
                        .authorizeRequests()
                    // secure API webservice
                    .anyRequest()
                            .authenticated()
                .and()
                   // disable csrf-protection
                   .csrf().disable();
        }
    }    

    /**
     * configure API-Configuration for Admin per HttpBasic-Auth
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
                    // secure path
                    .requestMatcher(new AntPathRequestMatcher("/admin/**", "GET"))
                        .authorizeRequests()
                        // secure API webservice
                        .anyRequest()
                            .hasRole("ADMIN")
                .and()
                   // disable csrf-protection
                   .csrf().disable();
        }
    }    

    /**
     * configure API-Configuration for Form-Auth (REST-API, Browsers..)
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
            http
                    // authentification
                    .formLogin()
                        .defaultSuccessUrl("/yaio-explorerapp/yaio-explorerapp.html#/frontpage", true)
                        // if set the defaultprocess doesnt match :-( loginPage(loginPage)
                .and() 
                    // authorize Requests
                    .authorizeRequests()
                        // allow CORS-Options Request
                        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // secure path
                        .antMatchers("/js/**", "/css/**", "/yaio-explorerapp/**", "/dist/**",
                                     "/converters/**", "/yaio-explorerapp/yaio-explorerapp.html",
                                     "/freemind-flash/**",
                                     "/examples/**", "/tests/**",
                                     "/user/current", "/login", "/logout",
                                     "/index.html", "/demo-public.html", "/demo.html")
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
                   .headers()
//                        .frameOptions()
//                        .disable()
//                        .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.ALLOW_FROM))
//                        .addHeaderWriter(new XFrameOptionsHeaderWriter(new WhiteListedAllowFromStrategy(WebSecurityConfig.getAllowedDomainList())))
                        // allow include as Frame for sameorigin
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN))
                 .and()
                   // add CsrfHeaderFilter because angular uses another Header
                   .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);
        }
    }    

    protected static List<String> getAllowedDomainList() {
        // extract allowed domains
        String xframeAllowedDomains = System.getProperty("yaio.my-domain", "dummy") 
                        + "," + System.getProperty("yaio.security.xframe-allowed-domains", "");
        List<String> xframeAllowedDomainsList = Arrays.asList(xframeAllowedDomains.split(","));
        for (String name : Configurator.getInstance().getKnownYaioInstances().keySet()) {
            Map<String, String> yaioInstance = Configurator.getInstance().getKnownYaioInstances().get(name);
            String url = yaioInstance.get(Configurator.CONST_PROPNAME_YAIOINSTANCES_URL);
            url = url.replace("http://", "");
            url = url.replace("https://", "");
            url = url.replace("/", "");
            url = url.replace(":", "");
            xframeAllowedDomainsList.add(url);
        }
        // xframeAllowedDomainsList = new ArrayList<String>();xframeAllowedDomainsList.add("*");
        logger.info("APIExportsSecurityConfigurerAdapter xframeallowOptions:" + xframeAllowedDomainsList);
        return xframeAllowedDomainsList;
    }
    
}
