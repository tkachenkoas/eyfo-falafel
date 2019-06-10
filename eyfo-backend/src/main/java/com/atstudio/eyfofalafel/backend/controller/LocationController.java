package com.atstudio.eyfofalafel.backend.controller;

import com.atstudio.eyfofalafel.backend.domain.place.Location;
import com.atstudio.eyfofalafel.backend.service.location.LocationService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("pretty")
    private Gson gson;

    public LocationController(LocationService locationService, Gson gson) {
        this.locationService = locationService;
        this.gson = gson;
    }

    @GetMapping("/address-suggestions")
    public List<String> getAddressSuggestions(@NotNull(message = "Search string must be at least 3 characters")
                                              @Size (min = 3, message = "Search string must be at least 3 characters")
                                              @RequestParam("searchStr") String searchStr) {
        return locationService.getAddressSuggestions(searchStr);
    }

    @GetMapping("/location-by-address")
    public Location getLocationByAddress(@NotNull(message = "Address must be at least 3 characters")
                                              @Size (min = 3, message = "Address must be at least 3 characters")
                                              @RequestParam("address") String address) {
        return locationService.getLocationByAddress(address);
    }

    @GetMapping(value = "/address-by-location", produces = "application/json")
    public String getAddressByLocation (@RequestParam("lat") Double lat, @RequestParam("lng") Double lng) {
        return gson.toJson(locationService.getAddressByLocation(Location.ofLatLng(lat,lng)));
    }

}
