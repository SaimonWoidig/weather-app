package cz.woidig.backend.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityUserDetailsTest {

    @Test
    void getAuthorities() {
        UserEntityUserDetails userEntityUserDetails = new UserEntityUserDetails("userId");

        Collection<? extends GrantedAuthority> authorities = userEntityUserDetails.getAuthorities();

        assertNotNull(authorities);
    }

    @Test
    void getPassword() {
        UserEntityUserDetails userEntityUserDetails = new UserEntityUserDetails("userId");

        String password = userEntityUserDetails.getPassword();

        assertNull(password);
    }

    @Test
    void getUsername() {
        UserEntityUserDetails userEntityUserDetails = new UserEntityUserDetails("userId");

        String username = userEntityUserDetails.getUsername();

        assertNotNull(username);
    }

    @Test
    void isAccountNonExpired() {
        UserEntityUserDetails userEntityUserDetails = new UserEntityUserDetails("userId");

        boolean accountNonExpired = userEntityUserDetails.isAccountNonExpired();

        assertTrue(accountNonExpired);
    }

    @Test
    void isAccountNonLocked() {
        UserEntityUserDetails userEntityUserDetails = new UserEntityUserDetails("userId");

        boolean accountNonLocked = userEntityUserDetails.isAccountNonLocked();

        assertTrue(accountNonLocked);
    }

    @Test
    void isCredentialsNonExpired() {
        UserEntityUserDetails userEntityUserDetails = new UserEntityUserDetails("userId");

        boolean credentialsNonExpired = userEntityUserDetails.isCredentialsNonExpired();

        assertTrue(credentialsNonExpired);
    }

    @Test
    void isEnabled() {
        UserEntityUserDetails userEntityUserDetails = new UserEntityUserDetails("userId");

        boolean enabled = userEntityUserDetails.isEnabled();

        assertTrue(enabled);
    }
}