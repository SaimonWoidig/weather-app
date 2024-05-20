package cz.woidig.backend.controller.user;

import cz.woidig.backend.dto.user.ChangePasswordDTO;
import cz.woidig.backend.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
}