package com.atstudio.eyfofalafel.backend.config.security;

import com.atstudio.eyfofalafel.backend.config.security.beans.CorsRequestFilter;
import com.atstudio.eyfofalafel.backend.service.security.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserSecurityService userSecurityService;

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
             .httpBasic().authenticationEntryPoint(failureHandler)
             .and().authorizeRequests()
             .antMatchers(PUBLIC_RESOURCES).permitAll()
             .antMatchers("/swagger-*").authenticated()
             .regexMatchers("/api/").authenticated();
        http.addFilterBefore(new CorsRequestFilter(), BasicAuthenticationFilter.class);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder);
    }

}
