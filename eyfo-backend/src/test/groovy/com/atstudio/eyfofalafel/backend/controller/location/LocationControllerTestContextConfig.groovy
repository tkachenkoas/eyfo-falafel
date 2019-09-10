package com.atstudio.eyfofalafel.backend.controller.location


import com.atstudio.eyfofalafel.backend.service.location.LocationServiceImpl
import com.atstudio.eyfofalafel.backend.service.location.google.GoogleApi
import com.atstudio.eyfofalafel.backend.service.location.google.GoogleLocationApi
import com.atstudio.eyfofalafel.backend.service.location.google.LocationObjectFactory
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

@TestConfiguration
@Import([LocationServiceImpl,LocationController, LocationObjectFactory, GoogleLocationApi])
class LocationControllerTestContextConfig {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper()
    }

    @MockBean
    GoogleApi googleApi

}