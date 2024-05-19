package cz.woidig.backend.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class RouteConfig {
    private final String[] unauthenticatedRoutes;
    private final String[] anonymousRoutes;
    private final String[] authenticatedRoutes;

    public RouteConfig() {
        this.unauthenticatedRoutes = new String[]{
                "/actuator/health"
        };
        this.anonymousRoutes = new String[]{
                "/auth/login",
                "/weather/current",
                "/geo/ip",
                "/geo/fallback"
        };
        this.authenticatedRoutes = new String[]{
                "/user/**"
        };
    }
}
