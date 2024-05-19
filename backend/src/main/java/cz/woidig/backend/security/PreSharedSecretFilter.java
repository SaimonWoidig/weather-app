package cz.woidig.backend.security;

import cz.woidig.backend.config.PreSharedSecretConfig;
import cz.woidig.backend.config.RouteConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class PreSharedSecretFilter extends OncePerRequestFilter {
    private final String SECRET_HEADER_KEY = "X-Secret";

    private final RouteConfig routeConfig;
    private final PreSharedSecretConfig preSharedSecretConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String secret = request.getHeader(SECRET_HEADER_KEY);
        if (secret == null || !secret.equals(preSharedSecretConfig.getPreSharedSecret())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "preSharedSecret",
                "",
                Collections.emptyList()
        );
        auth.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        List<String> routes = Arrays.asList(routeConfig.getAnonymousRoutes());
        boolean shouldFilter = routes.contains(path);
        return !shouldFilter;
    }
}
