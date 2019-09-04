package com.atstudio.eyfofalafel.backend.service.location;

import com.atstudio.eyfofalafel.backend.controller.location.LocationRestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationApi {

    List<String> getAddressSuggestions(String address);
    LocationRestDTO getLocationByAddress(String address);
    String getAddressByLocation(LocationRestDTO location);

}
