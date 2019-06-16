package com.atstudio.eyfofalafel.backend.controller.place;

import com.atstudio.eyfofalafel.backend.controller.beanmapper.SimpleRestObjectMapper;
import com.atstudio.eyfofalafel.backend.domain.place.Place;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("place")
public class PlaceRestMapper extends SimpleRestObjectMapper<Place, PlaceRestDto> {

    @Override
    protected Class<Place> getEntityClass() {
        return Place.class;
    }

    @Override
    protected Class<PlaceRestDto> getRestDtoClass() {
        return PlaceRestDto.class;
    }

}
