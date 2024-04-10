package cz.woidig.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class SecurityConfig {
    @Value("${backend.security.argon2.enabled}")
    private boolean argon2Enabled;
    @Value("${backend.security.argon2.memory}")
    private int memory;
    @Value("${backend.security.argon2.parallelism}")
    private int parallelism;
    @Value("${backend.security.argon2.iterations}")
    private int iterations;
    @Value("${backend.security.argon2.salt-length}")
    private int saltLength;
    @Value("${backend.security.argon2.hash-length}")
    private int hashLength;

    @Bean
    @ConditionalOnProperty("backend.security.argon2.enabled")
    public PasswordEncoder argon2PasswordEncoder() {
        log.info("The org.springframework.security.crypto.argon2.Argon2PasswordEncoder will be used as the password encoder.");
        if (memory < 1024) {
            throw new IllegalArgumentException("Argon2 memory must be atleast 1024.");
        }
        if (parallelism <= 0) {
            throw new IllegalArgumentException("Argon2 parallelism must be greater than 0.");
        }
        if (iterations <= 0) {
            throw new IllegalArgumentException("Argon2 iterations must be greater than 0.");
        }
        if (saltLength < 16) {
            throw new IllegalArgumentException("Argon2 salt length must be atleast 16.");
        }
        if (hashLength < 16) {
            throw new IllegalArgumentException("Argon2 hash length must be 16.");
        }

        return new Argon2PasswordEncoder(saltLength, hashLength, parallelism, memory, iterations);
    }

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    // Deprecation only symbolizes, that this encoder is unsafe which is okay in this case as it is only used as a fallback.
    @SuppressWarnings("deprecation")
    public PasswordEncoder noopPasswordEncoder() {
        log.warn("No password encoder configured. Using a no-op password encoder. This is unsafe and should be avoided.");
        return NoOpPasswordEncoder.getInstance();
    }
}
