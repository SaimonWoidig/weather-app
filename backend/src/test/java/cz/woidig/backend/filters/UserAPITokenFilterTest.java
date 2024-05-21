package cz.woidig.backend.filters;

import cz.woidig.backend.service.user.UserService;
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

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class UserAPITokenFilterTest {
    @Mock
    private UserService userService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    private UserAPITokenFilter userAPITokenFilter;

    @BeforeEach
    void setUp() {
        userAPITokenFilter = new UserAPITokenFilter(userService);
    }

    @Test
    void test_doFilterInternal_success() throws ServletException, IOException {
        String userId = "userId";
        String token = "token";
        Mockito.when(request.getHeader("X-Api-User")).thenReturn(userId);
        Mockito.when(request.getHeader("X-Api-Token")).thenReturn(token);
        Mockito.when(userService.isUserTokenIsValid(userId, token)).thenReturn(true);

        userAPITokenFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    void test_doFilterInternal_fail_noUserIdHeader() throws ServletException, IOException {
        Mockito.when(request.getHeader("X-Api-User")).thenReturn(null);

        userAPITokenFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    void test_doFilterInternal_fail_noTokenHeader() throws ServletException, IOException {
        Mockito.when(request.getHeader("X-Api-User")).thenReturn("userId");
        Mockito.when(request.getHeader("X-Api-Token")).thenReturn(null);

        userAPITokenFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    void test_doFilterInternal_fail_invalidToken() throws ServletException, IOException {
        String userId = "userId";
        String token = "token";
        Mockito.when(request.getHeader("X-Api-User")).thenReturn(userId);
        Mockito.when(request.getHeader("X-Api-Token")).thenReturn(token);
        Mockito.when(userService.isUserTokenIsValid(userId, token)).thenReturn(false);

        userAPITokenFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(filterChain).doFilter(request, response);
    }
}