package cz.woidig.backend.service.id;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class NanoIdGenerationServiceTest {
    private NanoIdGenerationService nanoIdGenerationService;

    @BeforeEach
    void setUp() {
        nanoIdGenerationService = new NanoIdGenerationService();
    }

    @Test
    void test_generateId() {
        String id = nanoIdGenerationService.generateId();
        assertNotNull(id);
    }
}