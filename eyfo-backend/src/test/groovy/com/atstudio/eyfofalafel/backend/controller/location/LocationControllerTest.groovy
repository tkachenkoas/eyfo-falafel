package com.atstudio.eyfofalafel.backend.controller.location

import com.atstudio.eyfofalafel.backend.controller.ControllerExceptionHandler
import com.atstudio.eyfofalafel.backend.service.location.LocationService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner)
@ContextConfiguration(classes = LocationControllerTestContextConfig)
class LocationControllerTest {

    @Autowired
    private LocationService locationService

    @Autowired
    private LocationController locationController

    private MockMvc mockMvc

    @Autowired
    private ObjectMapper objectMapper

    @Before
    void setUp() {
        Mockito.reset(locationService)
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(locationController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build()
    }

    @Test
    void testGetAddressSuggestions() {
        Mockito.when(locationService.getAddressSuggestions(Matchers.eq("Search address")))
                .thenReturn(["Address 1", "Address 2"])

        String result = mockMvc.perform(
                get("/location/address-suggestions")
                        .param("searchStr", "Search address")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
        List<String> response = objectMapper.readValue(result, String[]);

        assert response.size() == 2
        assert response[0] == 'Address 1'
        assert response[1] == 'Address 2'
    }

    @Test
    void testGetLocationByAddress() {
    }

    @Test
    void testGetAddressByLocation() {
    }
}
