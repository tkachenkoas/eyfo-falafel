package com.atstudio.eyfofalafel.backend.service.location;

import com.atstudio.eyfofalafel.backend.domain.place.Location;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private static Logger log;
    private LocationApi locationApi;

    public LocationServiceImpl(LocationApi locationApi, Logger logger) {
        this.locationApi = locationApi;
        this.log = logger;
    }

    @Override
    public List<String> getAddressSuggestions(String searchStr) {
        try {
            return this.locationApi.getAddressSuggestions(searchStr);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Location getLocationByAddress(String address) {
        try {
            return this.locationApi.getLocationByAddress(address);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String getAddressByLocation(Location location) {
        try {
            return this.locationApi.getAddressByLocation(location);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}
