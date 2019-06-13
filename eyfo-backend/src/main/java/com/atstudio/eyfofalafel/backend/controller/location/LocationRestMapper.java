package com.atstudio.eyfofalafel.backend.controller.location;

import com.atstudio.eyfofalafel.backend.controller.beanmapper.SimpleRestObjectMapper;
import com.atstudio.eyfofalafel.backend.domain.place.Location;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("location")
public class LocationRestMapper extends SimpleRestObjectMapper<Location, LocationRestDTO> {

    @Override
    protected Class<Location> getEntityClass() {
        return Location.class;
    }

    @Override
    protected Class<LocationRestDTO> getRestDtoClass() {
        return LocationRestDTO.class;
    }
}
