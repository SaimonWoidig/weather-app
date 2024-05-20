package cz.woidig.backend.filters;

import cz.woidig.backend.service.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class UserAPITokenFilter extends OncePerRequestFilter {
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userId = request.getHeader("X-Api-User");
        if (userId == null) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("X-Api-Token");
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!userService.isUserTokenIsValid(userId, token)) {
            filterChain.doFilter(request, response);
            return;
        }

        PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(userId, token, null);

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
