package cz.woidig.backend.service.user;

import cz.woidig.backend.dto.user.location.CreateSavedLocationDTO;
import cz.woidig.backend.dto.user.location.SavedLocationDTO;
import cz.woidig.backend.model.SavedLocation;
import cz.woidig.backend.model.SavedLocationRepository;
import cz.woidig.backend.model.User;
import cz.woidig.backend.service.id.IdGenerationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SavedLocationsService {
    private final UserService userService;
    private final SavedLocationRepository savedLocationRepository;
    private final IdGenerationService idGenerationService;

    public List<SavedLocationDTO> getSavedLocations(String userId) {
        Set<SavedLocation> savedLocations = savedLocationRepository.findAllByUser_UserId(userId);
        return savedLocations.stream().map(savedLocation -> new SavedLocationDTO(
                savedLocation.getLocationId(),
                savedLocation.getName(),
                savedLocation.getLatitude(),
                savedLocation.getLongitude())
        ).collect(Collectors.toCollection(ArrayList::new));
    }

    public SavedLocationDTO getSavedLocation(String locationId) {
        SavedLocation savedLocation = savedLocationRepository.findByLocationId(locationId);
        return new SavedLocationDTO(
                savedLocation.getLocationId(),
                savedLocation.getName(),
                savedLocation.getLatitude(),
                savedLocation.getLongitude()
        );
    }

    public SavedLocationDTO createSavedLocation(String userId, CreateSavedLocationDTO createSavedLocationDTO) {
        User user = userService.getUser(userId);
        String savedLocationId = idGenerationService.generateId();
        SavedLocation savedLocationEntity = new SavedLocation(
                savedLocationId,
                createSavedLocationDTO.name(),
                createSavedLocationDTO.latitude(),
                createSavedLocationDTO.longitude(),
                user
        );
        savedLocationRepository.save(savedLocationEntity);
        return new SavedLocationDTO(
                savedLocationId,
                createSavedLocationDTO.name(),
                createSavedLocationDTO.latitude(),
                createSavedLocationDTO.longitude()
        );
    }

    public void deleteLocation(String locationId) {
        savedLocationRepository.deleteByLocationId(locationId);
    }
}
