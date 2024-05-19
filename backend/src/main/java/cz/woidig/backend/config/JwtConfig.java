package cz.woidig.backend.config;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class JwtConfig {
    private final String jwtSecret;
    private final long jwtExpirationSeconds;
    private final long jwtLeewaySeconds;

    public JwtConfig(
            @Value("${backend.security.jwt.secret}") String jwtSecret,
            @Value("${backend.security.jwt.expiration-seconds}") long jwtExpirationSeconds,
            @Value("${backend.security.jwt.leeway-seconds}") long jwtLeewaySeconds
    ) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationSeconds = jwtExpirationSeconds;
        this.jwtLeewaySeconds = jwtLeewaySeconds;
    }

    @Bean
    public Algorithm hmac256JwtAlgorithm() {
        return Algorithm.HMAC256(jwtSecret);
    }
}
