package cz.woidig.backend.service.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import cz.woidig.backend.config.JwtConfig;
import cz.woidig.backend.model.User;
import cz.woidig.backend.security.UserEntityUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.time.Clock;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {
    @Mock
    private Algorithm algorithm;
    @Mock
    private JwtConfig jwtConfig;
    @Mock
    private Clock clock;
    @Mock
    private JWTVerifier verifier;
    @Mock
    private UserService userService;

    private JwtTokenService jwtTokenService;

    @BeforeEach
    void setUp() {
        jwtTokenService = new JwtTokenService(algorithm, jwtConfig, clock, verifier, userService);
    }

    @Test
    void test_isTokenValid_success() {
        Mockito.when(verifier.verify(Mockito.anyString())).thenReturn(Mockito.any(DecodedJWT.class));

        boolean expected = true;
        boolean actual = jwtTokenService.isTokenValid("userId");

        assertEquals(expected, actual);
    }

    @Test
    void test_isTokenValid_fail_nullToken() {
        boolean expected = false;
        boolean actual = jwtTokenService.isTokenValid(null);

        assertEquals(expected, actual);
    }

    @Test
    void test_isTokenValid_fail_emptyToken() {
        boolean expected = false;
        boolean actual = jwtTokenService.isTokenValid("");

        assertEquals(expected, actual);
    }

    @Test
    void test_isTokenValid_fail_invalidToken() {
        Mockito.when(verifier.verify(Mockito.anyString())).thenThrow(new JWTVerificationException("invalid token"));

        boolean expected = false;
        boolean actual = jwtTokenService.isTokenValid("userId");

        assertEquals(expected, actual);
    }

    @Test
    void test_getUserFromVerifiedToken_success() {
        DecodedJWT mockDecodedJWT = Mockito.mock(DecodedJWT.class);
        Mockito.when(mockDecodedJWT.getSubject()).thenReturn("userId");
        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(new User("userId", "email", "password"));
        try (MockedStatic<JWT> mock = Mockito.mockStatic(JWT.class)) {
            mock.when(() -> JWT.decode(Mockito.anyString())).thenReturn(mockDecodedJWT);

            UserEntityUserDetails expected = new UserEntityUserDetails("userId");
            UserEntityUserDetails actual = jwtTokenService.getUserFromVerifiedToken("token");

            assertTrue(new ReflectionEquals(expected).matches(actual));
            Mockito.verify(userService, Mockito.times(1)).getUser(Mockito.anyString());
        }
    }

    @Test
    void test_getUserFromVerifiedToken_fail_userIdNull() {
        DecodedJWT mockDecodedJWT = Mockito.mock(DecodedJWT.class);
        Mockito.when(mockDecodedJWT.getSubject()).thenReturn(null);
        try (MockedStatic<JWT> mock = Mockito.mockStatic(JWT.class)) {
            mock.when(() -> JWT.decode(Mockito.anyString())).thenReturn(mockDecodedJWT);

            UserEntityUserDetails actual = jwtTokenService.getUserFromVerifiedToken("token");

            assertNull(actual);
        }
    }

    @Test
    void test_getUserFromVerifiedToken_fail_userNotFound() {
        DecodedJWT mockDecodedJWT = Mockito.mock(DecodedJWT.class);
        Mockito.when(mockDecodedJWT.getSubject()).thenReturn("userId");
        Mockito.when(userService.getUser(Mockito.anyString())).thenThrow(new UsernameNotFoundException("user not found"));
        try (MockedStatic<JWT> mock = Mockito.mockStatic(JWT.class)) {
            mock.when(() -> JWT.decode(Mockito.anyString())).thenReturn(mockDecodedJWT);

            UserEntityUserDetails actual = jwtTokenService.getUserFromVerifiedToken("token");

            assertNull(actual);
            Mockito.verify(userService, Mockito.times(1)).getUser(Mockito.anyString());
        }
    }

    @Test
    void test_getPreAuthenticatedAuthenticationToken_success() {
        DecodedJWT mockDecodedJWT = Mockito.mock(DecodedJWT.class);
        Mockito.when(mockDecodedJWT.getSubject()).thenReturn("userId");
        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(new User("userId", "email", "password"));
        try (MockedStatic<JWT> mock = Mockito.mockStatic(JWT.class)) {
            mock.when(() -> JWT.decode(Mockito.anyString())).thenReturn(mockDecodedJWT);

            UserEntityUserDetails expectedUserDetails = new UserEntityUserDetails("userId");
            PreAuthenticatedAuthenticationToken expected = new PreAuthenticatedAuthenticationToken(
                    expectedUserDetails, "token", expectedUserDetails.getAuthorities()
            );
            PreAuthenticatedAuthenticationToken actual = jwtTokenService.getPreAuthenticatedAuthenticationToken("token");

            assertNotNull(actual);
            UserEntityUserDetails actualUserDetails = (UserEntityUserDetails) actual.getPrincipal();
            assertTrue(new ReflectionEquals(expectedUserDetails).matches(actualUserDetails));
            assertEquals(expected.isAuthenticated(), actual.isAuthenticated());
            assertEquals(expected.getAuthorities(), actual.getAuthorities());
            assertEquals(expected.getDetails(), actual.getDetails());
            assertEquals(expected.getName(), actual.getName());
            Mockito.verify(userService, Mockito.times(1)).getUser(Mockito.anyString());
        }
    }

    @Test
    void test_getPreAuthenticatedAuthenticationToken_fail_userDetailsNull() {
        DecodedJWT mockDecodedJWT = Mockito.mock(DecodedJWT.class);
        Mockito.when(mockDecodedJWT.getSubject()).thenReturn(null);
        try (MockedStatic<JWT> mock = Mockito.mockStatic(JWT.class)) {
            mock.when(() -> JWT.decode(Mockito.anyString())).thenReturn(mockDecodedJWT);
            PreAuthenticatedAuthenticationToken actual = jwtTokenService.getPreAuthenticatedAuthenticationToken("token");
            assertNull(actual);
        }
    }
}