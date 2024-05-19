package cz.woidig.backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class PreSharedSecretConfig {
    private final String preSharedSecret;

    public PreSharedSecretConfig(
            @Value("${backend.security.pre-shared-secret}") String preSharedSecret
    ) {
        if (preSharedSecret == null || preSharedSecret.isEmpty()) {
            throw new IllegalArgumentException("Pre-shared secret must be set.");
        }
        this.preSharedSecret = preSharedSecret;
    }
}
