package com.atstudio.eyfofalafel.backend.controller.location

import com.atstudio.eyfofalafel.backend.controller.MapperTestBase
import com.atstudio.eyfofalafel.backend.domain.place.Location
import org.junit.Test

class LocationRestMapperTest extends MapperTestBase {

    private LocationRestMapper mapper = new LocationRestMapper()

    @Test
    void getEntityClass() {
        LocationRestDTO restDTO = testLocationDto()
        Location location = mapper.toEntity(restDTO)
        assertLocationsEquality(restDTO, location)
    }

    @Test
    void getRestDtoClass() {
        Location location = testLocation()
        LocationRestDTO restDTO  = mapper.toRest(location)
        assertLocationsEquality(restDTO, location)
    }
}