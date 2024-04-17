package cz.woidig.backend.security;

import cz.woidig.backend.service.user.UserTokenService;
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

@Component
@AllArgsConstructor
@Slf4j
public class AppTokenFilter extends OncePerRequestFilter {
    private final String APP_TOKEN_HEADER_KEY = "X-App-Token";

    private final UserTokenService userTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String appToken = request.getHeader(APP_TOKEN_HEADER_KEY);
        if (appToken == null || appToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails userDetails = userTokenService.getUserFromAppToken(appToken);
        if (userDetails == null) {
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        auth.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);

        log.debug("Authenticated {} with app token", userDetails.getUsername());
        filterChain.doFilter(request, response);
    }
}
