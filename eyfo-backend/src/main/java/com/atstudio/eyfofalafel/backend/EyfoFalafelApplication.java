package com.atstudio.eyfofalafel.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class EyfoFalafelApplication {

	public static void main(String[] args) {
		SpringApplication.run(EyfoFalafelApplication.class, args);
	}

}
