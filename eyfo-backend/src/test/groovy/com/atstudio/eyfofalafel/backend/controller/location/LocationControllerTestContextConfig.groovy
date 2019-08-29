package com.atstudio.eyfofalafel.backend.controller.location


import com.atstudio.eyfofalafel.backend.controller.beanmapper.RestObjectMapper
import com.atstudio.eyfofalafel.backend.domain.place.Location
import com.atstudio.eyfofalafel.backend.service.location.LocationService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean

@TestConfiguration
class LocationControllerTestContextConfig {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper()
    }

    @MockBean
    LocationService locationService

    @Bean
    LocationController locationController() {
        return new LocationController(locationService, locationMapper())
    }

    RestObjectMapper<Location, LocationRestDTO> locationMapper() {
        return new LocationRestMapper();
    }

}
