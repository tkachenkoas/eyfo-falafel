package com.atstudio.eyfofalafel.backend.service.location;

import com.atstudio.eyfofalafel.backend.controller.location.LocationRestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationService {

    List<String> getAddressSuggestions(String searchStr);
    LocationRestDTO getLocationByAddress(String address);
    String getAddressByLocation(LocationRestDTO location);

}


