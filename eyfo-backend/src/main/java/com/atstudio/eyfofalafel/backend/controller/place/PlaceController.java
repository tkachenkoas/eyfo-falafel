package com.atstudio.eyfofalafel.backend.controller.place;

import com.atstudio.eyfofalafel.backend.domain.place.Place;
import com.atstudio.eyfofalafel.backend.service.place.PlaceFilter;
import com.atstudio.eyfofalafel.backend.service.place.PlaceService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Objects;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "/places")
@Slf4j
public class PlaceController {

    private PlaceService placeService;
    private PlaceRestMapper mapper;

    public PlaceController(PlaceService placeService, PlaceRestMapper mapper) {
        this.placeService = placeService;
        this.mapper = mapper;
    }

    @GetMapping("/")
    public ResponseEntity<List<PlaceRestDto>> search(
            @RequestParam(name = "filter", required = false) PlaceFilter filter,
            @RequestParam(name = "paging", required = false) Pageable paging
    ) {
        paging = ObjectUtils.firstNonNull(paging, PageRequest.of(1, 10));
        List<PlaceRestDto> places = placeService.findAll(ofNullable(filter), paging).stream()
                                                .map(pl -> mapper.toRest(pl))
                                                .collect(toList());
        return ResponseEntity.ok(places);
    }

    @PostMapping("/new")
    public ResponseEntity<PlaceRestDto> newPlace(@RequestBody PlaceRestDto place) {
        Place saved = placeService.save(mapper.toEntity(place));
        return ResponseEntity.ok(mapper.toRest(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceRestDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toRest(
                placeService.findByIdOrThrow(id)
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removeById(@PathVariable Long id) {
        placeService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @SneakyThrows
    public ResponseEntity<PlaceRestDto> editById(@RequestBody PlaceRestDto placeToSave, @PathVariable Long id) {
        if (!Objects.equals(placeToSave.getId(), id)) {
            throw new ValidationException("Place id must be present and be equal " + id);
        }
        return ResponseEntity.ok(mapper.toRest(
                            placeService.save(
                                    mapper.toEntity(placeToSave)
                            )
                    ));
    }

}
