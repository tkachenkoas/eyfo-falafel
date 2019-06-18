package com.atstudio.eyfofalafel.backend.controller.place;

import com.atstudio.eyfofalafel.backend.controller.beanmapper.RestObjectMapper;
import com.atstudio.eyfofalafel.backend.controller.beanmapper.SimpleRestObjectMapper;
import com.atstudio.eyfofalafel.backend.controller.location.LocationRestDTO;
import com.atstudio.eyfofalafel.backend.domain.place.Location;
import com.atstudio.eyfofalafel.backend.domain.place.Place;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("place")
public class PlaceRestMapper extends SimpleRestObjectMapper<Place, PlaceRestDto> {

    private RestObjectMapper<Location, LocationRestDTO> locationMapper;

    public PlaceRestMapper(@Qualifier("location") RestObjectMapper<Location, LocationRestDTO> locationMapper) {
        this.locationMapper = locationMapper;
    }

    @Override
    protected Class<Place> getEntityClass() {
        return Place.class;
    }

    @Override
    protected Class<PlaceRestDto> getRestDtoClass() {
        return PlaceRestDto.class;
    }

    public void patchExistingPlace(Place existingPlace, PlaceRestDto restDto) {
        existingPlace.setName(restDto.getName());
        existingPlace.setLocation(locationMapper.toEntity(restDto.getLocation()));
    }
}
