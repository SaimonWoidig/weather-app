package cz.woidig.backend.config;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public InetAddressValidator inetAddressValidator() {
        return InetAddressValidator.getInstance();
    }
}
