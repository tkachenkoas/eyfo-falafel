package com.atstudio.eyfofalafel.backend.service.place;

import com.atstudio.eyfofalafel.backend.domain.files.Attachment;
import com.atstudio.eyfofalafel.backend.domain.place.Place;
import com.atstudio.eyfofalafel.backend.repository.PlaceRepository;
import com.atstudio.eyfofalafel.backend.service.files.FileStorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class PlaceServiceImpl implements PlaceService {

    private PlaceRepository crudRepo;
    private FileStorageService fileStorageService;

    public PlaceServiceImpl(PlaceRepository crudRepo, FileStorageService fileStorageService) {
        this.crudRepo = crudRepo;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public Page<Place> findAll(Optional<PlaceFilter> filterOptional, Pageable paging) {
        return filterOptional
                    .map(filter -> crudRepo.findFiltered(filter, paging))
                    .orElseGet(() -> crudRepo.findAll(paging));
    }

    @Override
    @Transactional
    public Place save(Place placeToSave) {
        List<Attachment> newAttachments = storeAll(placeToSave.getAttachments());

        if (placeToSave.getId() == null) {
            placeToSave.setAttachments(newAttachments);
            return crudRepo.save(placeToSave);
        }

        Place existingPlace = findByIdOrThrow(placeToSave.getId());
        for (Attachment existing: existingPlace.getAttachments()) {
            if (!newAttachments.contains(existing)){
                fileStorageService.remove(existing);
            }
        }
        existingPlace.setName(placeToSave.getName());
        existingPlace.setLocation(placeToSave.getLocation());
        existingPlace.setAttachments(newAttachments);

        return crudRepo.save(existingPlace);
    }

    private List<Attachment> storeAll(List<Attachment> attachments) {
        return attachments.stream()
                          .map(att -> fileStorageService.saveFileForStorage(att))
                          .collect(toList());
    }

    @Override
    public Place findByIdOrThrow(Long id)  {
        return crudRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Can't find place with id=" + id));
    }

    @Override
    public void deleteById(Long id) {
        this.crudRepo.deleteById(id);
    }
}
