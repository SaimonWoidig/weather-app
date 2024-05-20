package cz.woidig.backend.controller.user;

import cz.woidig.backend.dto.user.ChangePasswordDTO;
import cz.woidig.backend.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class SettingsController {
    private final UserService userService;

    @PutMapping("/{userId}/settings/password")
    @PreAuthorize("principal.username == #userId")
    public void changePassword(@PathVariable String userId, @RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.changeUserPassword(userId, changePasswordDTO.newPassword());
    }
}
