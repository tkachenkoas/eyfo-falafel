package com.atstudio.eyfofalafel.backend;

import com.atstudio.eyfofalafel.backend.service.files.LocalStorageFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class EyfoFalafelApplication {

	public static void main(String[] args) {
		SpringApplication.run(EyfoFalafelApplication.class, args);
	}

	@Bean
	public static LocalStorageFileService localStorageFileService(@Value("${files.drive.folder}") String fileStorageLocation) {
		return new LocalStorageFileService(fileStorageLocation);
	}

}
