package com.atstudio.eyfofalafel.backend.controller.place;

import com.atstudio.eyfofalafel.backend.domain.place.Place;
import com.atstudio.eyfofalafel.backend.service.place.PlaceFilter;
import com.atstudio.eyfofalafel.backend.service.place.PlaceService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.Objects;

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
    public ResponseEntity<Page<PlaceRestDto>> search(
            PlaceFilter filter,
            @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<Place> placePage = placeService.findAll(filter, paging);

        Page<PlaceRestDto> restPlacesPage = new PageImpl<>(
                placePage.stream().map(mapper::toRest).collect(toList()),
                placePage.getPageable(),
                placePage.getTotalElements()
        );
        return ResponseEntity.ok(restPlacesPage);
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
