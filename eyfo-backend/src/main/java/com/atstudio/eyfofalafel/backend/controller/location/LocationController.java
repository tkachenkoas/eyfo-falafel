package com.atstudio.eyfofalafel.backend.controller.location;

import com.atstudio.eyfofalafel.backend.controller.beanmapper.RestObjectMapper;
import com.atstudio.eyfofalafel.backend.domain.place.Location;
import com.atstudio.eyfofalafel.backend.service.location.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping(value = "/location")
@Slf4j
public class LocationController {

    private LocationService locationService;
    private RestObjectMapper<Location, LocationRestDTO> mapper;

    public LocationController(LocationService locationService,
                              @Qualifier("location") RestObjectMapper mapper) {
        this.locationService = locationService;
        this.mapper = mapper;
    }

    @GetMapping("/address-suggestions")
    public ResponseEntity<List<String>> getAddressSuggestions(@NotNull(message = "Search string must be at least 3 characters")
                                              @Size (min = 3, message = "Search string must be at least 3 characters")
                                              @RequestParam("searchStr") String searchStr) {
        return ResponseEntity.ok(locationService.getAddressSuggestions(searchStr));
    }

    @GetMapping("/location-by-address")
    public ResponseEntity<LocationRestDTO> getLocationByAddress(@NotNull(message = "Address must be at least 3 characters")
                                              @Size (min = 3, message = "Address must be at least 3 characters")
                                              @RequestParam("address") String address) {
        LocationRestDTO response = mapper.toRest(locationService.getLocationByAddress(address));
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/address-by-location", produces = "application/json")
    public ResponseEntity<LocationRestDTO> getAddressByLocation (@RequestParam("lat") Double lat, @RequestParam("lng") Double lng) {
        Location location = Location.ofLatLng(lat,lng);
        location.setAddress(locationService.getAddressByLocation(location));
        return ResponseEntity.ok(mapper.toRest(location));
    }

}
