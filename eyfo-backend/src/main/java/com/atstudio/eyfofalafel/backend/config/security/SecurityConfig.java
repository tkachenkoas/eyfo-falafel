package com.atstudio.eyfofalafel.backend.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@Profile("!integration-tests")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userSecurityService;

    @Autowired
    private AuthenticationEntryPoint failureHandler;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private static final String[] PUBLIC_RESOURCES = {
            "/css/**",
            "/js/**",
            "/image/**",
            "/api/user/**",
            "/api/login",
            "/error"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
             .csrf().disable()
             .cors().disable()
             .authorizeRequests()
                .antMatchers(PUBLIC_RESOURCES).permitAll()
                .antMatchers("/swagger-*").authenticated()
                .antMatchers("/api/**").authenticated()
                .and()
                .httpBasic().authenticationEntryPoint(failureHandler)
        ;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder);
    }

}
