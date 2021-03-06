/** 
 * software for projectmanagement and documentation
 * 
 * @FeatureDomain                Collaboration 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package de.yaio.app.server.config;

import de.yaio.app.config.YaioConfiguration;
import de.yaio.commons.io.IOExceptionWithCause;
import de.yaio.commons.io.IOUtils;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * yaio-websecurity-config
 *  
 * @FeatureDomain                Webservice Config
 * @package                      de.yaio.server
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
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
                        .frameOptions().sameOrigin()
//                        .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.ALLOW_FROM))
//                        .addHeaderWriter(new XFrameOptionsHeaderWriter(new WhiteListedAllowFromStrategy(WebSecurityConfig.getAllowedDomainList())));
                        // allow include as Frame for sameorigin
//                        .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN))
                        ;
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
     * configure API-Configuration for Admin per HttpBasic-Auth
     */
    @EnableWebSecurity
    @Configuration
    @Order(4)
    public static class APIServicesWebSecurityConfigurerAdapter extends APIWebSecurityConfigurerAdapter {
        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            http
                    // authentification
                    .httpBasic()
                .and()
                    // secure path
                    .requestMatcher(new AntPathRequestMatcher("/services/**"))
                        .authorizeRequests()
                        // secure API webservice
                        .anyRequest()
                            .hasRole("SERVICES")
                .and()
                   // disable csrf-protection
                   .csrf().disable()
                   .headers()
                        .frameOptions().sameOrigin()
                   ;
        }
    }    

    /**
     * configure API-Configuration for Form-Auth (REST-API, Browsers..)
     */
    @EnableWebSecurity
    @Configuration
    @Order(5)
    public static class APIWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        private CsrfTokenRepository csrfTokenRepository() {
          HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
          repository.setHeaderName("X-XSRF-TOKEN");
          return repository;
        }        

        @Autowired
        public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
            Properties users;
            try {
                users = IOUtils.getInstance().readProperties(System.getProperty(CONST_FILELOCATION_APIUSERS));
            } catch (IOExceptionWithCause ex) {
                throw new IllegalArgumentException("cant read propertyFile for AuthenticationManager", ex);
            }
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
                                     "/converters/**", "/apiconfig/**", "/yaio-explorerapp/yaio-explorerapp.html",
                                     "/freemind-flash/**",
                                     "/fixtures/**", "/examples/**", "/tests/**",
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
                        .frameOptions().sameOrigin()
//                        .disable()
//                        .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.ALLOW_FROM))
//                        .addHeaderWriter(new XFrameOptionsHeaderWriter(new WhiteListedAllowFromStrategy(WebSecurityConfig.getAllowedDomainList())))
                        // allow include as Frame for sameorigin
                        //.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN))
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

        Map<String, Map<String, String>> knownYaioInstances = YaioConfiguration.getInstance().getKnownYaioInstances();
        for (String name : knownYaioInstances.keySet()) {
            Map<String, String> yaioInstance = knownYaioInstances.get(name);
            String url = yaioInstance.get(YaioConfiguration.CONST_PROPNAME_YAIOINSTANCES_URL);
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
