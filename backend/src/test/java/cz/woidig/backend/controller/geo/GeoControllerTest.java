package cz.woidig.backend.controller.geo;

import cz.woidig.backend.dto.geo.GeoDTO;
import cz.woidig.backend.exceptions.GeoException;
import cz.woidig.backend.service.geo.GeoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class GeoControllerTest {
    @Mock
    private GeoService geoService;

    private GeoController geoController;

    @BeforeEach
    void setUp() {
        geoController = new GeoController(geoService);
    }

    @Test
    void test_getGeo_success() {
        Mockito.doReturn(new GeoDTO(0.0f, 0.0f))
                .when(geoService).getGeoByIp(Mockito.anyString());

        assertDoesNotThrow(() -> geoController.getGeoByIP("1.1.1.1"));
    }

    @Test
    void test_getGeo_throws() {
        Mockito.doThrow(new GeoException("Error"))
                .when(geoService).getGeoByIp(Mockito.anyString());
        assertThrows(GeoException.class, () -> geoController.getGeoByIP("1.1.1.1"));
    }
}