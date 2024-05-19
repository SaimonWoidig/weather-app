package cz.woidig.backend.config;

import cz.woidig.backend.security.ApiTokenFilter;
import cz.woidig.backend.security.JwtAuthFilter;
import cz.woidig.backend.security.PreSharedSecretFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SpringWebSecurityConfig {
    private final RouteConfig routeConfig;
    private final JwtAuthFilter jwtAuthFilter;
    private final ApiTokenFilter apiTokenFilter;
    private final PreSharedSecretFilter preSharedSecretFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/actuator/**").permitAll()
//                        .requestMatchers("/auth/**").permitAll()
//                        .requestMatchers("/weather/current").permitAll()
//                        .requestMatchers("/geo/**").permitAll()
//                        .requestMatchers("/user/**").permitAll()
//                        .anyRequest().authenticated()
//                                .requestMatchers(routeConfig.getUnauthenticatedRoutes()).permitAll()
                                .requestMatchers("/actuator/health").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(apiTokenFilter, JwtAuthFilter.class)
                .addFilterBefore(preSharedSecretFilter, ApiTokenFilter.class);

        return http.build();
    }
}
