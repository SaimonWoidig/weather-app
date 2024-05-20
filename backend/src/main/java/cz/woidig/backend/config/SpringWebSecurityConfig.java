package cz.woidig.backend.config;

import cz.woidig.backend.filters.JwtAuthFilter;
import cz.woidig.backend.filters.UserAPITokenFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SpringWebSecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final UserAPITokenFilter userAPITokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/weather/current").permitAll()
                .requestMatchers("/geo/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
        );
        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilterBefore(jwtAuthFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(userAPITokenFilter, JwtAuthFilter.class);
        return http.build();
    }
}
