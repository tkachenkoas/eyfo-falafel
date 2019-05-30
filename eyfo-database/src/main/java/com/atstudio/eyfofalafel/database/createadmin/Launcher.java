package com.atstudio.eyfofalafel.database.createadmin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

@SpringBootApplication
@PropertySource("seeders/admin-user.properties")
public class Launcher {

    @Value("${security.password.salt}")
    private String SALT;

    @Value("${security.password.strength}")
    private Integer STRENGTH;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(STRENGTH, new SecureRandom(SALT.getBytes()));
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Launcher.class, args);
        ctx.getBean(AdminUserCreator.class).createAdminUser();
        ctx.close();
    }

}
