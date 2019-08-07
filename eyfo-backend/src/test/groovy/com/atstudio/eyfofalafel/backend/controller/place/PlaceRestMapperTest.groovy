package com.atstudio.eyfofalafel.backend.controller.place

import com.atstudio.eyfofalafel.backend.controller.BaseMapperTest
import com.atstudio.eyfofalafel.backend.controller.files.FilesObjectMapper
import com.atstudio.eyfofalafel.backend.controller.location.LocationRestMapper
import com.atstudio.eyfofalafel.backend.controller.place.PlaceRestDto
import com.atstudio.eyfofalafel.backend.controller.place.PlaceRestMapper
import com.atstudio.eyfofalafel.backend.domain.place.Place
import org.junit.Test

class PlaceRestMapperTest extends BaseMapperTest {

    private PlaceRestMapper mapper = new PlaceRestMapper(new LocationRestMapper(), new FilesObjectMapper())

    @Test
    void toEntity() {
        PlaceRestDto restDto = new PlaceRestDto()
        restDto.setName("name")
        restDto.setPriceFrom(BigDecimal.ONE)
        restDto.setLocation(testLocationDto())

        Place place = mapper.toEntity(restDto)

        assert restDto.getName() == place.getName()
        assert restDto.getPriceFrom() == place.getPriceFrom()
        compareLocations(restDto.getLocation(), place.getLocation())
    }

    @Test
    void toRest() {
        Place place = new Place()
        place.setName("name")
        place.setPriceFrom(BigDecimal.ONE)
        place.setLocation(testLocation())

        PlaceRestDto restDto = mapper.toRest(place)

        assert  restDto.getName() == place.getName()
        assert restDto.getPriceFrom() == place.getPriceFrom()
        compareLocations(restDto.getLocation(), place.getLocation())
    }
}