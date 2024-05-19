package cz.woidig.backend.config.password;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class Argon2PasswordEncoderConfig {
    private final int memory;
    private final int parallelism;
    private final int iterations;
    private final int saltLength;
    private final int hashLength;

    public Argon2PasswordEncoderConfig(
            @Value("${backend.security.argon2.memory}") int memory,
            @Value("${backend.security.argon2.parallelism}") int parallelism,
            @Value("${backend.security.argon2.iterations}") int iterations,
            @Value("${backend.security.argon2.salt-length}") int saltLength,
            @Value("${backend.security.argon2.hash-length}") int hashLength
    ) {
        log.info("The org.springframework.security.crypto.argon2.Argon2PasswordEncoder will be used as the password encoder.");
        this.memory = memory;
        this.parallelism = parallelism;
        this.iterations = iterations;
        this.saltLength = saltLength;
        this.hashLength = hashLength;
    }

    @Bean
    @ConditionalOnProperty("backend.security.argon2.enabled")
    public PasswordEncoder argon2PasswordEncoder() {
        return new Argon2PasswordEncoder(saltLength, hashLength, parallelism, memory, iterations);
    }
}