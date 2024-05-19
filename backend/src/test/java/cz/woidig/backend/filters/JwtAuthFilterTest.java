package cz.woidig.backend.filters;

import cz.woidig.backend.service.user.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private JwtTokenService jwtTokenService;

    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        jwtAuthFilter = new JwtAuthFilter(jwtTokenService);
    }

    @Test
    void test_doFilterInternal_fail_AuthHeaderMissing() throws ServletException, IOException {
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    void test_doFilterInternal_fail_AuthHeaderInvalid() throws ServletException, IOException {
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("invalid");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    void test_doFilterInternal_fail_AuthHeaderEmpty() throws ServletException, IOException {
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    void test_doFilterInternal_fail_AuthHeaderOnlyBearerSpace() throws ServletException, IOException {
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer ");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    void test_doFilterInternal_fail_JwtTokenInvalid() throws ServletException, IOException {
        String token = "token";
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        Mockito.when(jwtTokenService.isTokenValid("token")).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    void test_doFilterInternal_fail_NullPreAuthToken() throws ServletException, IOException {
        String token = "token";
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        Mockito.when(jwtTokenService.isTokenValid("token")).thenReturn(true);
        Mockito.when(jwtTokenService.getPreAuthenticatedAuthenticationToken("token")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    void test_doFilterInternal_success() throws ServletException, IOException {
        String token = "token";
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        Mockito.when(jwtTokenService.isTokenValid("token")).thenReturn(true);
        Mockito.when(jwtTokenService.getPreAuthenticatedAuthenticationToken("token"))
                .thenReturn(new PreAuthenticatedAuthenticationToken("user", null, null));

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(filterChain).doFilter(request, response);
    }
}