package com.atstudio.eyfofalafel.backend.controller.location


import com.atstudio.eyfofalafel.backend.entities.place.Location
import com.vividsolutions.jts.geom.GeometryFactory
import org.junit.Test

import static com.atstudio.eyfofalafel.backend.controller.MapperHelper.assertLocationsEquality
import static com.atstudio.eyfofalafel.backend.controller.MapperHelper.testLocation
import static com.atstudio.eyfofalafel.backend.controller.MapperHelper.testLocationDto

class LocationRestMapperTest {

    private LocationRestMapper mapper = new LocationRestMapper(new GeometryFactory())

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