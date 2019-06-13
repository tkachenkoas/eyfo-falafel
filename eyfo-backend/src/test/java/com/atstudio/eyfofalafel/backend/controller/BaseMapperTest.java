package com.atstudio.eyfofalafel.backend.controller;

import com.atstudio.eyfofalafel.backend.controller.location.LocationRestDTO;
import com.atstudio.eyfofalafel.backend.domain.place.Location;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class BaseMapperTest {

    protected Location testLocation() {
        Location location = new Location();
        location.setAddress("address");
        location.setLatitude(BigDecimal.ONE);
        location.setLongitude(BigDecimal.TEN);
        return location;
    }

    protected LocationRestDTO testLocationDto() {
        LocationRestDTO restDTO = new LocationRestDTO();
        restDTO.setAddress("address");
        restDTO.setLatitude(BigDecimal.ONE);
        restDTO.setLongitude(BigDecimal.TEN);
        return restDTO;
    }

    protected void compareLocations(LocationRestDTO restDTO, Location location) {
        assertEquals(location.getAddress(), restDTO.getAddress());
        assertEquals(location.getLatitude(), restDTO.getLatitude());
        assertEquals(location.getLongitude(), restDTO.getLongitude());
    }

}