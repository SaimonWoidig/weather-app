package cz.woidig.backend.service.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import cz.woidig.backend.config.JwtConfig;
import cz.woidig.backend.model.User;
import cz.woidig.backend.security.UserEntityUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
@AllArgsConstructor
public class JwtTokenService {
    private final Algorithm algorithm;
    private final JwtConfig jwtConfig;
    private final Clock clock;
    private final JWTVerifier verifier;
    private final UserService userService;

    public String createUserJwt(String userId) {
        return JWT.create()
                .withSubject(userId)
                .withIssuedAt(clock.instant())
                .withNotBefore(clock.instant())
                .withExpiresAt(clock.instant().plusSeconds(jwtConfig.getJwtExpirationSeconds()))
                .sign(algorithm);
    }

    public boolean isTokenValid(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public UserEntityUserDetails getUserFromVerifiedToken(String token) {
        String userId = JWT.decode(token).getSubject();
        if (userId == null) {
            return null;
        }
        try {
            User user = userService.getUser(userId);
            return new UserEntityUserDetails(user.getUserId());
        } catch (UsernameNotFoundException e) {
            return null;
        }
    }

    public PreAuthenticatedAuthenticationToken getPreAuthenticatedAuthenticationToken(String token) {
        UserEntityUserDetails userDetails = getUserFromVerifiedToken(token);
        if (userDetails == null) {
            return null;
        }
        return new PreAuthenticatedAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }
}