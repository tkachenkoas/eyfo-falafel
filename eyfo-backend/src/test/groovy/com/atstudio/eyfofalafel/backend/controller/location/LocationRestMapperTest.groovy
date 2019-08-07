package com.atstudio.eyfofalafel.backend.controller.location

import com.atstudio.eyfofalafel.backend.controller.BaseMapperTest
import com.atstudio.eyfofalafel.backend.controller.location.LocationRestDTO
import com.atstudio.eyfofalafel.backend.controller.location.LocationRestMapper
import com.atstudio.eyfofalafel.backend.domain.place.Location
import org.junit.Test

class LocationRestMapperTest extends BaseMapperTest {

    private LocationRestMapper mapper = new LocationRestMapper()

    @Test
    void getEntityClass() {
        LocationRestDTO restDTO = testLocationDto()
        Location location = mapper.toEntity(restDTO)
        compareLocations(restDTO, location)
    }

    @Test
    void getRestDtoClass() {
        Location location = testLocation()
        LocationRestDTO restDTO  = mapper.toRest(location)
        compareLocations(restDTO, location)
    }
}