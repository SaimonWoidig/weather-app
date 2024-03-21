package cz.woidig.backend.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorDTOTest {

    @Test
    void testErrorDTO() {
        int code = 500;
        String message = "Internal server error";
        ErrorDTO errorDTO = new ErrorDTO(code, message);
        assertEquals(code, errorDTO.code());
        assertEquals(message, errorDTO.message());
    }

}