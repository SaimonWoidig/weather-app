package cz.woidig.backend.security;

import cz.woidig.backend.config.RouteConfig;
import cz.woidig.backend.model.User;
import cz.woidig.backend.service.user.ApiTokenService;
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
public class ApiTokenFilter extends OncePerRequestFilter {
    private final String APP_TOKEN_HEADER_KEY = "X-Api-Token";

    private final RouteConfig routeConfig;
    private final ApiTokenService apiTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String apiToken = request.getHeader(APP_TOKEN_HEADER_KEY);
        if (apiToken == null || apiToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        User user = apiTokenService.getUserFromApiToken(apiToken);
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

        log.debug("Authenticated {} with apiToken", user.getUid());
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
