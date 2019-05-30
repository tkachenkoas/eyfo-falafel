package com.atstudio.eyfofalafel.backend.resource;

import com.atstudio.eyfofalafel.backend.domain.place.Place;
import com.atstudio.eyfofalafel.backend.service.place.PlaceService;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(value = "/places")
public class PlaceResource {

    private static Logger log;
    private PlaceService placeService;

    public PlaceResource(Logger logger, PlaceService placeService) {
        this.log = logger;
        this.placeService = placeService;
    }

    @GetMapping("/")
    public List<Place> all() {
        return placeService.findAll();
    }

    @PostMapping("/new")
    public Place newPlace(@RequestBody Place place) {
        return placeService.save(place);
    }

    @GetMapping("/{id}")
    public Place getById(@PathVariable Long id) {
        return placeService.findById(id).orElseThrow(() -> new EntityNotFoundException("id"));
    }

    @PutMapping("/{id}")
    public Place editById(@RequestBody Place place) {
        return placeService.save(place);
    }


}
