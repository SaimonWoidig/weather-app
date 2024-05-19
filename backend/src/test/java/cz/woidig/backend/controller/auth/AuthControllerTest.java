package cz.woidig.backend.controller.auth;

import cz.woidig.backend.dto.user.LoginDTO;
import cz.woidig.backend.dto.user.RegisterUserDTO;
import cz.woidig.backend.dto.user.UserDTO;
import cz.woidig.backend.service.user.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    private AuthService authService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(authService);
    }

    @Test
    void test_register() {
        String email = "email";
        String password = "password";
        String token = "token";
        String userId = "userId";

        Mockito.when(authService.registerUser(email, password)).thenReturn(new UserDTO(email, userId, token));

        UserDTO expected = new UserDTO(email, userId, token);
        UserDTO actual = authController.register(new RegisterUserDTO(email, password));

        assertEquals(expected, actual);
    }

    @Test
    void test_login() {
        String email = "email";
        String password = "password";
        String token = "token";
        String userId = "userId";

        Mockito.when(authService.loginUser(email, password)).thenReturn(new UserDTO(email, userId, token));

        UserDTO expected = new UserDTO(email, userId, token);
        UserDTO actual = authController.login(new LoginDTO(email, password));

        assertEquals(expected, actual);
    }
}