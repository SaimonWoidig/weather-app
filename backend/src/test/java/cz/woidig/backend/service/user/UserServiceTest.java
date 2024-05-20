package cz.woidig.backend.service.user;

import cz.woidig.backend.model.User;
import cz.woidig.backend.model.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void test_getUser_success() {
        String userId = "userId";
        Mockito.when(userRepository.findUserByUserId(userId))
                .thenReturn(Optional.of(new User(userId, "email", "password")));
        User expected = new User(userId, "email", "password");
        User actual = userService.getUser(userId);

        Mockito.verify(userRepository).findUserByUserId(userId);
        assertTrue(new ReflectionEquals(expected).matches(actual));
    }

    @Test
    void test_getUser_fail_userNotFound() {
        Mockito.when(userRepository.findUserByUserId(Mockito.anyString()))
                .thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.getUser(Mockito.anyString()));
    }

    @Test
    void test_changePassword_success() {
        String userId = "userId";
        String newPassword = "newPassword";
        User user = new User(userId, "email", "oldPasswordHash");
        Mockito.when(userRepository.findUserByUserId(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn(Mockito.anyString());

        userService.changeUserPassword(userId, newPassword);

        Mockito.verify(passwordEncoder).encode(newPassword);
        Mockito.verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    void test_changePassword_fail_userNotFound() {
        String userId = "userId";
        String newPassword = "newPassword";
        Mockito.when(userRepository.findUserByUserId(Mockito.anyString())).thenReturn(Optional.empty());
        
        assertThrows(UsernameNotFoundException.class, () -> userService.changeUserPassword(userId, newPassword));
    }
}