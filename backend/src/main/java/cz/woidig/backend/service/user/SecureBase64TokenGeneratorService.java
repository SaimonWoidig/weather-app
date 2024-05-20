package cz.woidig.backend.service.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@AllArgsConstructor
public class SecureBase64TokenGeneratorService implements TokenGeneratorService {
    private final Base64.Encoder base64Encoder;
    private final SecureRandom secureRandom;

    public final int DEFAULT_BYTE_LENGTH = 32;

    @Override
    public String generateToken() {
        byte[] randomBytes = new byte[DEFAULT_BYTE_LENGTH];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
