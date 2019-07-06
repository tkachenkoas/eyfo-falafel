package com.atstudio.eyfofalafel.backend.controller.place;

import com.atstudio.eyfofalafel.backend.controller.beanmapper.RestObjectMapper;
import com.atstudio.eyfofalafel.backend.controller.beanmapper.SimpleRestObjectMapper;
import com.atstudio.eyfofalafel.backend.controller.files.FilesObjectMapper;
import com.atstudio.eyfofalafel.backend.controller.location.LocationRestDTO;
import com.atstudio.eyfofalafel.backend.domain.place.Location;
import com.atstudio.eyfofalafel.backend.domain.place.Place;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Component
@Qualifier("place")
public class PlaceRestMapper extends SimpleRestObjectMapper<Place, PlaceRestDto> {

    private RestObjectMapper<Location, LocationRestDTO> locationMapper;
    private FilesObjectMapper filelsMapper;

    public PlaceRestMapper(@Qualifier("location") RestObjectMapper<Location, LocationRestDTO> locationMapper,
                           FilesObjectMapper filelsMapper) {
        this.locationMapper = locationMapper;
        this.filelsMapper = filelsMapper;
    }

    @Override
    protected Class<Place> getEntityClass() {
        return Place.class;
    }

    @Override
    protected Class<PlaceRestDto> getRestDtoClass() {
        return PlaceRestDto.class;
    }

    @Override
    public Place toEntity(PlaceRestDto restObject) {
        Place autoResult = super.toEntity(restObject);
        autoResult.setAttachments(emptyIfNull(restObject.getAttachments()).stream()
                                            .map(att -> filelsMapper.fromRestDto(att)).collect(toList()));
        return autoResult;
    }

    @Override
    public PlaceRestDto toRest(Place entity) {
        PlaceRestDto autoResult = super.toRest(entity);
        autoResult.setAttachments(emptyIfNull(entity.getAttachments()).stream()
                                        .map(att -> filelsMapper.fromAttachment(att)).collect(toList()));
        return autoResult;
    }

}
