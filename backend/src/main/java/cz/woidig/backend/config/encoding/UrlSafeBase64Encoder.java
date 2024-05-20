package cz.woidig.backend.config.encoding;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

@Configuration
public class UrlSafeBase64Encoder {
    @Bean
    public Base64.Encoder urlSafeBase64NoPaddingEncoder() {
        return Base64.getUrlEncoder().withoutPadding();
    }
}
