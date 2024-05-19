package cz.woidig.backend.service.user;

import cz.woidig.backend.dto.user.UserDTO;
import cz.woidig.backend.exceptions.InvalidPasswordException;
import cz.woidig.backend.exceptions.UserAlreadyExistsException;
import cz.woidig.backend.model.User;
import cz.woidig.backend.model.UserRepository;
import cz.woidig.backend.service.id.IdGenerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private IdGenerationService idGenerationService;
    @Mock
    private JwtTokenService jwtTokenService;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordEncoder, idGenerationService, jwtTokenService);
    }

    @Test
    void test_registerUser_success() {
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(idGenerationService.generateId()).thenReturn("userId");
        Mockito.when(jwtTokenService.createUserJwt(Mockito.anyString())).thenReturn("jwtToken");

        UserDTO expected = new UserDTO("email@test.com", "userId", "jwtToken");
        UserDTO actual = authService.registerUser("email@test.com", "password");

        assertEquals(expected, actual);
        Mockito.verify(userRepository).save(Mockito.any(User.class));
        Mockito.verify(idGenerationService).generateId();
        Mockito.verify(jwtTokenService).createUserJwt(Mockito.anyString());
    }

    @Test
    void test_registerUser_fail_userAlreadyExists() {
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authService.registerUser("email@test.com", "password"));
    }

    @Test
    void test_loginUser_success() {
        Mockito.when(userRepository.findUserByEmail(Mockito.anyString()))
                .thenReturn(Optional.of(new User("userId", "email@test.com", "passwordHash")));
        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.when(jwtTokenService.createUserJwt(Mockito.anyString())).thenReturn("jwtToken");

        UserDTO expected = new UserDTO("email@test.com", "userId", "jwtToken");
        UserDTO actual = authService.loginUser("email@test.com", "password");

        assertEquals(expected, actual);
    }

    @Test
    void test_loginUser_fail_userNotFound() {
        Mockito.when(userRepository.findUserByEmail(Mockito.anyString()))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authService.loginUser("email@test.com", "password"));
    }

    @Test
    void test_loginUser_fail_invalidPassword() {
        Mockito.when(userRepository.findUserByEmail(Mockito.anyString()))
                .thenReturn(Optional.of(new User("userId", "email@test.com", "passwordHash")));
        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () -> authService.loginUser("email@test.com", "password"));
    }
}