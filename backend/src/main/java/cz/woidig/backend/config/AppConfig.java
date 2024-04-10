package cz.woidig.backend.config;

import cz.woidig.backend.utils.id.IDGenerator;
import cz.woidig.backend.utils.id.NanoIDGenerator;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
public class AppConfig {
    @Bean
    public InetAddressValidator inetAddressValidator() {
        return InetAddressValidator.getInstance();
    }

    @Bean
    public IDGenerator idGenerator() {
        return new NanoIDGenerator(
                new SecureRandom()
        );
    }
}
