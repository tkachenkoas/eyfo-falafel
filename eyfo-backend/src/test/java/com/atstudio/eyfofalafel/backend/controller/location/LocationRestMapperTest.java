package com.atstudio.eyfofalafel.backend.controller.location;

import com.atstudio.eyfofalafel.backend.controller.BaseMapperTest;
import com.atstudio.eyfofalafel.backend.domain.place.Location;
import org.junit.Test;

public class LocationRestMapperTest extends BaseMapperTest {

    private LocationRestMapper mapper = new LocationRestMapper();

    @Test
    public void getEntityClass() {
        LocationRestDTO restDTO = testLocationDto();
        Location location = mapper.toEntity(restDTO);
        compareLocations(restDTO, location);
    }

    @Test
    public void getRestDtoClass() {
        Location location = testLocation();
        LocationRestDTO restDTO  = mapper.toRest(location);
        compareLocations(restDTO, location);
    }
}