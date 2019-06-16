package com.atstudio.eyfofalafel.backend.controller.place;

import com.atstudio.eyfofalafel.backend.domain.place.Place;
import com.atstudio.eyfofalafel.backend.service.place.PlaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/places")
@Slf4j
public class PlaceController {

    private PlaceService placeService;
    private PlaceRestMapper mapper;

    public PlaceController(PlaceService placeService, @Qualifier("place") PlaceRestMapper mapper) {
        this.placeService = placeService;
        this.mapper = mapper;
    }

    @GetMapping("/")
    public ResponseEntity<List<PlaceRestDto>> all() {
        List<PlaceRestDto> places = placeService.findAll().stream().map(pl -> mapper.toRest(pl)).collect(Collectors.toList());
        return ResponseEntity.ok(places);
    }

    @PostMapping("/new")
    public ResponseEntity<PlaceRestDto> newPlace(@RequestBody PlaceRestDto place) {
        Place saved = placeService.save(mapper.toEntity(place));
        return ResponseEntity.ok(mapper.toRest(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                placeService.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Can't find place with id=" + id))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaceRestDto> editById(@RequestBody PlaceRestDto place) {
        Place saved = placeService.save(mapper.toEntity(place));
        return ResponseEntity.ok(mapper.toRest(saved));
    }


}