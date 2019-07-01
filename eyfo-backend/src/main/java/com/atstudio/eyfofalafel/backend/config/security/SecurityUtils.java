package com.atstudio.eyfofalafel.backend.config.security;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

@Configuration
public class SecurityUtils  {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(@Value("${security.password.salt}") String salt,
                                                 @Value("${security.password.strength}")Integer strength) {
        return new BCryptPasswordEncoder(strength, new SecureRandom(salt.getBytes()));
    }

    @Bean
    public String randomPassword(){
        int length = 10;
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

}
