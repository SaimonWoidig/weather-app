package cz.woidig.backend.service.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import cz.woidig.backend.config.JwtConfig;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
@AllArgsConstructor
public class JwtVerifierProvider {
    private final Algorithm algorithm;
    private final JwtConfig jwtConfig;
    private final Clock clock;

    @Bean
    public JWTVerifier jwtVerifier() {
        JWTVerifier.BaseVerification baseVerification = (JWTVerifier.BaseVerification) JWT
                .require(algorithm)
                .acceptLeeway(jwtConfig.getJwtLeewaySeconds());
        return baseVerification.build(clock);
    }
}
