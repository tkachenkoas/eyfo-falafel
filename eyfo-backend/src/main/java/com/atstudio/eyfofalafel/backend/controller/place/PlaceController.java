package com.atstudio.eyfofalafel.backend.controller.place;

import com.atstudio.eyfofalafel.backend.domain.place.Place;
import com.atstudio.eyfofalafel.backend.service.place.PlaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Place> newPlace(@RequestBody Place place) {
        return ResponseEntity.ok(placeService.save(place));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                placeService.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Can't find place with id=" + id))
        );
    }

    @PutMapping("/{id}")
    public Place editById(@RequestBody Place place) {
        return placeService.save(place);
    }


}
