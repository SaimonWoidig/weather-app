package cz.woidig.backend.controller.user;

import cz.woidig.backend.dto.user.ChangePasswordDTO;
import cz.woidig.backend.dto.user.IsTokenValidDTO;
import cz.woidig.backend.dto.user.NewApiTokenDTO;
import cz.woidig.backend.dto.user.ValidateTokenDTO;
import cz.woidig.backend.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SettingsControllerTest {
    @Mock
    private UserService userService;

    private SettingsController settingsController;

    @BeforeEach
    void setUp() {
        settingsController = new SettingsController(userService);
    }

    @Test
    void test_changePassword() {
        String userId = "userId";
        String newPassword = "newPassword";

        settingsController.changePassword(userId, new ChangePasswordDTO(newPassword));

        Mockito.verify(userService).changeUserPassword(userId, newPassword);
    }

    @Test
    void test_newApiToken() {
        String userId = "userId";
        String token = "token";
        Mockito.when(userService.newApiToken(userId)).thenReturn(new NewApiTokenDTO(token));

        NewApiTokenDTO expected = new NewApiTokenDTO(token);
        NewApiTokenDTO actual = settingsController.newApiToken(userId);

        assertEquals(expected, actual);
    }

    @Test
    void test_validateApiToken_success() {
        String userId = "userId";
        String token = "token";
        Mockito.when(userService.isUserTokenIsValid(userId, token)).thenReturn(true);

        IsTokenValidDTO expected = new IsTokenValidDTO(true);
        IsTokenValidDTO actual = settingsController.validateApiToken(userId, new ValidateTokenDTO(token));

        assertEquals(expected, actual);
    }

    @Test
    void test_validateApiToken_fail_tokenInvalid() {
        String userId = "userId";
        String token = "token";
        Mockito.when(userService.isUserTokenIsValid(userId, token)).thenReturn(false);

        IsTokenValidDTO expected = new IsTokenValidDTO(false);
        IsTokenValidDTO actual = settingsController.validateApiToken(userId, new ValidateTokenDTO(token));

        assertEquals(expected, actual);
    }
}