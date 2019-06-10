package com.atstudio.eyfofalafel.backend.controller;

import com.atstudio.eyfofalafel.backend.domain.place.Place;
import com.atstudio.eyfofalafel.backend.service.place.PlaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(value = "/places")
@Slf4j
public class PlaceController {

    private PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Place>> all() {
        List<Place> places = placeService.findAll();
        return ResponseEntity.ok(places);
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
