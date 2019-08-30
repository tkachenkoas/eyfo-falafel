package com.atstudio.eyfofalafel.backend.controller.location;

import com.atstudio.eyfofalafel.backend.service.location.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/location")
@Slf4j
public class LocationController {

    private LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/address-suggestions")
    public ResponseEntity<List<String>> getAddressSuggestions(@RequestParam("searchStr") String searchStr) {
        return ResponseEntity.ok(locationService.getAddressSuggestions(searchStr));
    }

    @GetMapping("/location-by-address")
    public ResponseEntity<LocationRestDTO> getLocationByAddress(@RequestParam("address") String address) {
        return ResponseEntity.ok(
                locationService.getLocationByAddress(address)
        );
    }

    @GetMapping(value = "/address-by-location", produces = "application/json")
    public ResponseEntity<LocationRestDTO> getAddressByLocation (@RequestParam("lat") Double lat, @RequestParam("lng") Double lng) {
        LocationRestDTO location = LocationRestDTO.ofLatLng(lat,lng);
        location.setAddress(locationService.getAddressByLocation(location));
        return ResponseEntity.ok(location);
    }

}
