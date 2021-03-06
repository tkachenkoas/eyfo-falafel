package com.atstudio.eyfofalafel.backend.controller.place;

import com.atstudio.eyfofalafel.backend.controller.beanmapper.SimpleRestObjectMapper;
import com.atstudio.eyfofalafel.backend.controller.files.FilesObjectMapper;
import com.atstudio.eyfofalafel.backend.controller.location.LocationRestMapper;
import com.atstudio.eyfofalafel.backend.entities.place.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Component
public class PlaceRestMapper extends SimpleRestObjectMapper<Place, PlaceRestDto> {

    private final FilesObjectMapper filesMapper;
    private final LocationRestMapper locationRestMapper;

    @Autowired
    public PlaceRestMapper(FilesObjectMapper filesMapper, LocationRestMapper locationRestMapper) {
        super();
        this.filesMapper = filesMapper;
        this.locationRestMapper = locationRestMapper;
    }

    @Override
    public Place toEntity(PlaceRestDto restObject) {
        Place autoResult = super.toEntity(restObject);
        autoResult.setLocation(locationRestMapper.toEntity(restObject.getLocation()));
        autoResult.setAttachments(
                emptyIfNull(restObject.getAttachments()).stream()
                        .map(filesMapper::fromRestDto)
                        .collect(toList())
        );
        return autoResult;
    }

    @Override
    public PlaceRestDto toRest(Place entity) {
        PlaceRestDto autoResult = super.toRest(entity);
        autoResult.setLocation(locationRestMapper.toRest(entity.getLocation()));
        autoResult.setAttachments(
                emptyIfNull(entity.getAttachments()).stream()
                        .map(filesMapper::fromAttachment)
                        .collect(toList())
        );
        return autoResult;
    }

    @Override
    public PlaceRestDto toShortRest(Place entity) {
        return super.toRest(entity);
    }
}
