package com.atstudio.eyfofalafel.backend.service.place

import com.atstudio.eyfofalafel.backend.TestDataSourceAutoConfiguration
import com.atstudio.eyfofalafel.backend.service.files.FileStorageService
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

import static org.mockito.Mockito.mock

@Configuration
@EnableJpaRepositories(basePackages=["com.atstudio.eyfofalafel.backend.repository"])
@Import([PlaceServiceImpl, TestDataSourceAutoConfiguration])
@EntityScan("com.atstudio.eyfofalafel.backend.domain")
class PlaceServiceTestConfig {

    @Bean
    FileStorageService fileStorageService(){
        return mock(FileStorageService)
    }

}
