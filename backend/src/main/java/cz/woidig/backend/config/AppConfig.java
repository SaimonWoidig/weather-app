package cz.woidig.backend.config;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class AppConfig {
    @Bean
    public UriBuilder uriBuilder() {
        return UriComponentsBuilder.newInstance();
    }

    @Bean
    public InetAddressValidator inetAddressValidator() {
        return InetAddressValidator.getInstance();
    }
}
