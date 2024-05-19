package cz.woidig.backend.service.user;

import cz.woidig.backend.dto.user.location.CreateSavedLocationDTO;
import cz.woidig.backend.dto.user.location.SavedLocationDTO;
import cz.woidig.backend.model.SavedLocation;
import cz.woidig.backend.model.SavedLocationRepository;
import cz.woidig.backend.model.User;
import cz.woidig.backend.service.id.IdGenerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SavedLocationsServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private SavedLocationRepository savedLocationRepository;
    @Mock
    private IdGenerationService idGenerationService;

    private SavedLocationsService savedLocationsService;

    @BeforeEach
    void setUp() {
        savedLocationsService = new SavedLocationsService(userService, savedLocationRepository, idGenerationService);
    }

    @Test
    void test_getSavedLocations() {
        String userId = "userId";
        User user = new User(userId, "email", "password");
        Set<SavedLocation> savedLocations = Set.of(
                new SavedLocation("loc1", "location 1", 0.0f, 0.0f, user),
                new SavedLocation("loc2", "location 2", 0.0f, 0.0f, user)
        );
        Mockito.when(savedLocationRepository.findAllByUser_UserId(userId)).thenReturn(savedLocations);
        List<SavedLocationDTO> expected = List.of(
                new SavedLocationDTO("loc1", "location 1", 0.0f, 0.0f),
                new SavedLocationDTO("loc2", "location 2", 0.0f, 0.0f)
        );
        List<SavedLocationDTO> actual = savedLocationsService.getSavedLocations(userId);

        Mockito.verifyNoMoreInteractions(userService, savedLocationRepository, idGenerationService);
        assertTrue(expected.size() == actual.size() && expected.containsAll(actual) && actual.containsAll(expected));
    }

    @Test
    void test_getSavedLocation() {
        String userId = "userId";
        String locationId = "loc1";
        User user = new User(userId, "email", "password");
        SavedLocation savedLocation = new SavedLocation(locationId, "location 1", 0.0f, 0.0f, user);
        Mockito.when(savedLocationRepository.findByLocationId(locationId)).thenReturn(savedLocation);

        SavedLocationDTO expected = new SavedLocationDTO(locationId, "location 1", 0.0f, 0.0f);
        SavedLocationDTO actual = savedLocationsService.getSavedLocation(locationId);

        Mockito.verifyNoMoreInteractions(userService, savedLocationRepository, idGenerationService);
        assertEquals(expected, actual);
    }

    @Test
    void test_deleteLocation() {
        String locationId = "loc1";
        savedLocationsService.deleteLocation(locationId);
        Mockito.verify(savedLocationRepository).deleteByLocationId(locationId);
    }

    @Test
    void test_createSavedLocation() {
        String userId = "userId";
        String name = "location 1";
        String locationId = "loc1";
        User user = new User(userId, "email", "password");
        CreateSavedLocationDTO createSavedLocationDTO = new CreateSavedLocationDTO(name, 0.0f, 0.0f);
        Mockito.when(idGenerationService.generateId()).thenReturn(locationId);
        Mockito.when(userService.getUser(userId)).thenReturn(user);

        SavedLocationDTO expected = new SavedLocationDTO(locationId, name, 0.0f, 0.0f);
        SavedLocationDTO actual = savedLocationsService.createSavedLocation(userId, createSavedLocationDTO);

        Mockito.verify(savedLocationRepository).save(Mockito.any(SavedLocation.class));
        Mockito.verifyNoMoreInteractions(userService, idGenerationService);
        assertTrue(new ReflectionEquals(expected).matches(actual));
    }
}