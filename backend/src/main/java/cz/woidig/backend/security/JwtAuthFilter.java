package cz.woidig.backend.security;

import cz.woidig.backend.config.RouteConfig;
import cz.woidig.backend.model.User;
import cz.woidig.backend.service.user.JwtTokenService;
import cz.woidig.backend.service.user.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final RouteConfig routeConfig;
    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);
        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtTokenService.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        User user = jwtTokenService.getUserFromVerifiedToken(token);
        if (user == null) {
            filterChain.doFilter(request, response);
            return;
        }
        UserDetails userDetails = UserPrincipal.createFromUser(user);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        auth.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);

        log.debug("Authenticated {} with JWT", user.getUid());
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        List<String> routes = Arrays.asList(routeConfig.getAuthenticatedRoutes());
        boolean shouldFilter = routes.contains(path);
        return !shouldFilter;
    }
}
