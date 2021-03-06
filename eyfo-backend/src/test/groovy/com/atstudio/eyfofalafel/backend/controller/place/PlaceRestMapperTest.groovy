package com.atstudio.eyfofalafel.backend.controller.place


import com.atstudio.eyfofalafel.backend.controller.files.FilesObjectMapper
import com.atstudio.eyfofalafel.backend.controller.location.LocationRestMapper
import com.atstudio.eyfofalafel.backend.entities.place.Place
import com.vividsolutions.jts.geom.GeometryFactory
import org.junit.Test

import static com.atstudio.eyfofalafel.backend.controller.MapperHelper.assertLocationsEquality
import static com.atstudio.eyfofalafel.backend.controller.MapperHelper.testLocation
import static com.atstudio.eyfofalafel.backend.controller.MapperHelper.testLocationDto

class PlaceRestMapperTest {

    private PlaceRestMapper mapper = new PlaceRestMapper(
            new FilesObjectMapper(),
            new LocationRestMapper(new GeometryFactory())
    )

    @Test
    void toEntity() {
        PlaceRestDto restDto = new PlaceRestDto()
        restDto.setName("name")
        restDto.setPriceFrom(BigDecimal.ONE)
        restDto.setLocation(testLocationDto())

        Place place = mapper.toEntity(restDto)

        assert restDto.getName() == place.getName()
        assert restDto.getPriceFrom() == place.getPriceFrom()
        assertLocationsEquality(restDto.getLocation(), place.getLocation())
    }

    @Test
    void toRest() {
        Place place = new Place()
        place.setName("name")
        place.setPriceFrom(BigDecimal.ONE)
        place.setLocation(testLocation())

        PlaceRestDto restDto = mapper.toRest(place)

        assert restDto.getName() == place.getName()
        assert restDto.getPriceFrom() == place.getPriceFrom()
        assertLocationsEquality(restDto.getLocation(), place.getLocation())
    }
}