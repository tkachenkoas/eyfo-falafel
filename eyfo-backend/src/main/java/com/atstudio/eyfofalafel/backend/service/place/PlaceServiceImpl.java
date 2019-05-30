package com.atstudio.eyfofalafel.backend.service.place;

import com.atstudio.eyfofalafel.backend.repository.PlaceRepository;
import org.springframework.stereotype.Service;

@Service
public class PlaceServiceImpl implements PlaceService {

    private PlaceRepository crudRepo;

    public PlaceServiceImpl(PlaceRepository crudRepo) {
        this.crudRepo = crudRepo;
    }

    @Override
    public PlaceRepository getCrudRepository() {
        return crudRepo;
    }
}
