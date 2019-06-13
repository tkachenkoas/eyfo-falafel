package com.atstudio.eyfofalafel.backend.controller.place;

import com.atstudio.eyfofalafel.backend.controller.BaseMapperTest;
import com.atstudio.eyfofalafel.backend.controller.location.LocationRestMapper;
import com.atstudio.eyfofalafel.backend.domain.place.Place;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class PlaceRestMapperTest extends BaseMapperTest {

    private PlaceRestMapper mapper = new PlaceRestMapper();

    @Test
    public void toEntity() {
        PlaceRestDto restDto = new PlaceRestDto();
        restDto.setName("name");
        restDto.setPriceFrom(BigDecimal.ONE);
        restDto.setLocation(testLocationDto());

        Place place = mapper.toEntity(restDto);

        assertEquals(restDto.getName(), place.getName());
        assertEquals(restDto.getPriceFrom(), place.getPriceFrom());
        compareLocations(restDto.getLocation(), place.getLocation());
    }

    @Test
    public void toRest() {
        Place place = new Place();
        place.setName("name");
        place.setPriceFrom(BigDecimal.ONE);
        place.setLocation(testLocation());

        PlaceRestDto restDto = mapper.toRest(place);

        assertEquals(restDto.getName(), place.getName());
        assertEquals(restDto.getPriceFrom(), place.getPriceFrom());
        compareLocations(restDto.getLocation(), place.getLocation());
    }
}