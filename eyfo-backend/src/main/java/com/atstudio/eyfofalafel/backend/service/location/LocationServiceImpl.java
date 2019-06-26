package com.atstudio.eyfofalafel.backend.service.location;

import com.atstudio.eyfofalafel.backend.domain.place.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LocationServiceImpl implements LocationService {

    private LocationApi locationApi;

    public LocationServiceImpl(LocationApi locationApi) {
        this.locationApi = locationApi;
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
