package cz.woidig.backend.controller.user;

import cz.woidig.backend.dto.user.location.CreateSavedLocationDTO;
import cz.woidig.backend.dto.user.location.SavedLocationDTO;
import cz.woidig.backend.service.user.SavedLocationsService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class SavedLocationsController {
    private final SavedLocationsService savedLocationsService;

    @GetMapping("/{userId}/locations")
    @PreAuthorize("principal.username == #userId")
    public List<SavedLocationDTO> getSavedLocations(@PathVariable String userId) {
        return savedLocationsService.getSavedLocations(userId);
    }

    @GetMapping("/{userId}/locations/{locationId}")
    @PreAuthorize("principal.username == #userId")
    public SavedLocationDTO getSavedLocation(@PathVariable String userId, @PathVariable String locationId) {
        return savedLocationsService.getSavedLocation(locationId);
    }

    @DeleteMapping("/{userId}/locations/{locationId}")
    @PreAuthorize("principal.username == #userId")
    public void deleteLocation(@PathVariable String userId, @PathVariable String locationId) {
        savedLocationsService.deleteLocation(locationId);
    }

    @PostMapping("/{userId}/locations")
    @PreAuthorize("principal.username == #userId")
    public SavedLocationDTO createSavedLocation(@PathVariable String userId, @RequestBody CreateSavedLocationDTO createSavedLocationDTO) {
        return savedLocationsService.createSavedLocation(userId, createSavedLocationDTO);
    }
}
