package com.atstudio.eyfofalafel.backend.service.location;

import com.atstudio.eyfofalafel.backend.controller.location.LocationRestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private LocationApi locationApi;

    @Autowired
    public LocationServiceImpl(LocationApi locationApi) {
        this.locationApi = locationApi;
    }

    @Override
    public List<String> getAddressSuggestions(String searchStr) {
        return this.locationApi.getAddressSuggestions(searchStr);
    }

    @Override
    public LocationRestDTO getLocationByAddress(String address) {
        return this.locationApi.getLocationByAddress(address);
    }

    @Override
    public String getAddressByLocation(LocationRestDTO location) {
        return this.locationApi.getAddressByLocation(location);
    }

}
