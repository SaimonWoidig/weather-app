package cz.woidig.backend.config.password;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class NoopPasswordEncoderConfig {
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    // Deprecation only symbolizes, that this encoder is unsafe which is okay in this case as it is only used as a fallback.
    @SuppressWarnings("deprecation")
    public PasswordEncoder noopPasswordEncoder() {
        log.warn("No password encoder configured. Using a no-op password encoder. This is unsafe and should be avoided.");
        return NoOpPasswordEncoder.getInstance();
    }
}
