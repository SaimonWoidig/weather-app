package cz.woidig.backend.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.SecureRandom;
import java.util.Base64;

@ExtendWith(MockitoExtension.class)
class SecureBase64TokenGeneratorServiceTest {
    @Mock
    private Base64.Encoder base64Encoder;
    @Mock
    private SecureRandom secureRandom;

    private SecureBase64TokenGeneratorService secureBase64TokenGeneratorService;

    @BeforeEach
    void setUp() {
        secureBase64TokenGeneratorService = new SecureBase64TokenGeneratorService(base64Encoder, secureRandom);
    }

    @Test
    void test_generateToken() {
        secureBase64TokenGeneratorService.generateToken();
    }
}