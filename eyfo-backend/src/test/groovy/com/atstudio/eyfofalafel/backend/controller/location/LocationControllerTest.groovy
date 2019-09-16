package com.atstudio.eyfofalafel.backend.controller.location

import com.atstudio.eyfofalafel.backend.controller.ControllerExceptionHandler
import com.atstudio.eyfofalafel.backend.service.location.google.GoogleApi
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.maps.model.AutocompletePrediction
import com.google.maps.model.GeocodingResult
import com.google.maps.model.Geometry
import com.google.maps.model.LatLng
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner)
@ContextConfiguration(classes = LocationControllerTestContextConfig)
class LocationControllerTest {

    @Autowired
    private GoogleApi googleApi

    @Autowired
    private LocationController locationController

    private MockMvc mockMvc

    @Autowired
    private ObjectMapper objectMapper

    @Before
    void setUp() {
        Mockito.reset(googleApi)
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(locationController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build()
    }

    @Test
    void testGetAddressSuggestions() {
        when(googleApi.getPredictions(eq("Search address")))
                .thenReturn(
                        [prediction("Address 1"),
                         prediction("Address 2")] as AutocompletePrediction[]
                )

        List<String> response = objectMapper.readValue(
                performGetWithParamsAndExtractResponseString("/location/address-suggestions", ["searchStr": "Search address"] as Map),
                String[]);

        assert response.size() == 2
        assert response[0] == 'Address 1'
        assert response[1] == 'Address 2'
    }

    @Test
    void testGetLocationByAddress() {
        LocationRestDTO mockResponse = [
                "address": "Final address",
                "latitude": 50,
                "longitude": 100
        ] as LocationRestDTO

        when(googleApi.getGeoCodings(eq("Search address")))
                .thenReturn(
                        [geocoding(
                                mockResponse.getAddress(),
                                mockResponse.getLatitude(),
                                mockResponse.getLongitude()
                        )] as GeocodingResult[]
                )

        LocationRestDTO response = objectMapper.readValue(
                performGetWithParamsAndExtractResponseString("/location/location-by-address", ["address": "Search address"] as Map),
                LocationRestDTO);

        assert sameLocation(response, mockResponse)
    }

    static boolean sameLocation(LocationRestDTO expected, Object actual) {
        return actual['address'] == expected.getAddress() &&
                actual['latitude'] == expected.getLatitude() &&
                actual['longitude'] == expected.getLongitude()
    }

    @Test
    void testGetAddressByLocation() {

        LocationRestDTO reqLocation = [
                "latitude": 50,
                "longitude": 100
        ] as LocationRestDTO

        LatLng latLng = new LatLng(reqLocation.latitude, reqLocation.longitude)
        when(googleApi.reversedGeoCodings(eq(latLng)))
                .thenReturn(
                        [geocoding("Target address", 50, 100)] as GeocodingResult[]
                )

        LocationRestDTO response = objectMapper.readValue(
                performGetWithParamsAndExtractResponseString("/location/address-by-location", ["lat": 50, "lng": 100] as Map),
                LocationRestDTO);

        assert response.longitude == reqLocation.longitude &&
                response.latitude == reqLocation.latitude &&
                response.address == "Target address"
    }

    private static AutocompletePrediction prediction(String description) {
        return new AutocompletePrediction(description: description)
    }

    private static GeocodingResult geocoding(String address, Double lat, Double lng) {
        def result = new GeocodingResult();
        result.geometry = new Geometry();
        result.geometry.location = new LatLng();
        result.geometry.location.lat = lat
        result.geometry.location.lng = lng
        result.formattedAddress = address
        return result
    }

    private String performGetWithParamsAndExtractResponseString(
            String url,
            Map<String, Object> params
    ) {
        MockHttpServletRequestBuilder getBuilder = get(url);
        for (Map.Entry<String, Object> param: params.entrySet()) {
            getBuilder.param(param.getKey(), param.getValue() as String)
        }
        return mockMvc.perform(getBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString()
    }
}
