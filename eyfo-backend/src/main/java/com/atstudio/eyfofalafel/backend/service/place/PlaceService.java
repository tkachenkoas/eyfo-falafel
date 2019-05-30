package com.atstudio.eyfofalafel.backend.service.place;

import com.atstudio.eyfofalafel.backend.domain.place.Place;
import com.atstudio.eyfofalafel.backend.service.general.CrudService;
import org.springframework.stereotype.Service;

@Service
public interface PlaceService extends CrudService<Place, Long> {

}
