package cz.woidig.backend.service.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import cz.woidig.backend.config.JwtConfig;
import cz.woidig.backend.model.User;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class JwtTokenService {
    private final Algorithm algorithm;
    private final JwtConfig jwtConfig;
    private final Clock clock;
    private final JWTVerifier verifier;
    private final UserService userService;

    public JwtTokenService(Algorithm algorithm, JwtConfig jwtConfig, Clock clock, UserService userService) {
        this.algorithm = algorithm;
        this.jwtConfig = jwtConfig;
        this.clock = clock;
        this.verifier = JWT.require(algorithm)
                .acceptLeeway(jwtConfig.getJwtLeewaySeconds())
                .build();
        this.userService = userService;
    }

    public String createUserJwt(User user) {
        return JWT.create()
                .withSubject(user.getUid())
                .withIssuedAt(clock.instant())
                .withNotBefore(clock.instant())
                .withExpiresAt(clock.instant().plusMillis(jwtConfig.getJwtExpirationSeconds()))
                .sign(algorithm);
    }

    public boolean isTokenValid(String token) {
        try {
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public User getUserFromVerifiedToken(String token) {
        String userId = JWT.decode(token).getSubject();
        if (userId == null) {
            return null;
        }
        return userService.getUser(userId);
    }

}
