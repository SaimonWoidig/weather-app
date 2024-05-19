package cz.woidig.backend.controller.user;

import cz.woidig.backend.dto.user.location.CreateSavedLocationDTO;
import cz.woidig.backend.dto.user.location.SavedLocationDTO;
import cz.woidig.backend.service.user.SavedLocationsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SavedLocationsControllerTest {
    @Mock
    private SavedLocationsService savedLocationsService;

    private SavedLocationsController savedLocationsController;

    @BeforeEach
    void setUp() {
        savedLocationsController = new SavedLocationsController(savedLocationsService);
    }

    @Test
    void test_getSavedLocations() {
        String userId = "userId";
        List<SavedLocationDTO> expected = List.of(
                new SavedLocationDTO("loc1", "location 1", 0.0f, 0.0f)
        );
        Mockito.when(savedLocationsService.getSavedLocations(userId)).thenReturn(expected);
        List<SavedLocationDTO> actual = savedLocationsController.getSavedLocations(userId);

        Mockito.verify(savedLocationsService).getSavedLocations(userId);
        Mockito.verifyNoMoreInteractions(savedLocationsService);
        assertEquals(expected, actual);
    }

    @Test
    void test_getSavedLocation() {
        String userId = "userId";
        String locationId = "loc1";
        SavedLocationDTO expected = new SavedLocationDTO("loc1", "location 1", 0.0f, 0.0f);
        Mockito.when(savedLocationsService.getSavedLocation(locationId)).thenReturn(expected);
        SavedLocationDTO actual = savedLocationsController.getSavedLocation(userId, locationId);

        Mockito.verify(savedLocationsService).getSavedLocation(locationId);
        Mockito.verifyNoMoreInteractions(savedLocationsService);
        assertEquals(expected, actual);
    }

    @Test
    void test_deleteLocation() {
        String userId = "userId";
        String locationId = "loc1";
        savedLocationsController.deleteLocation(userId, locationId);
        Mockito.verify(savedLocationsService).deleteLocation(locationId);
        Mockito.verifyNoMoreInteractions(savedLocationsService);
    }

    @Test
    void test_createSavedLocation() {
        String userId = "userId";
        CreateSavedLocationDTO createSavedLocationDTO = new CreateSavedLocationDTO("location1", 0.0f, 0.0f);
        SavedLocationDTO expected = new SavedLocationDTO("loc1", "location 1", 0.0f, 0.0f);
        Mockito.when(savedLocationsService.createSavedLocation(userId, createSavedLocationDTO)).thenReturn(expected);
        SavedLocationDTO actual = savedLocationsController.createSavedLocation(userId, createSavedLocationDTO);

        Mockito.verify(savedLocationsService).createSavedLocation(userId, createSavedLocationDTO);
        Mockito.verifyNoMoreInteractions(savedLocationsService);
        assertEquals(expected, actual);
    }
}