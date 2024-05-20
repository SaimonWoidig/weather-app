package cz.woidig.backend.controller.user;

import cz.woidig.backend.dto.user.ChangePasswordDTO;
import cz.woidig.backend.dto.user.IsTokenValidDTO;
import cz.woidig.backend.dto.user.NewApiTokenDTO;
import cz.woidig.backend.dto.user.ValidateTokenDTO;
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

    @PostMapping("/{userId}/settings/token")
    @PreAuthorize("principal.username == #userId")
    public NewApiTokenDTO newApiToken(@PathVariable String userId) {
        return userService.newApiToken(userId);
    }

    @PostMapping("/{userId}/settings/token/validate")
    @PreAuthorize("principal.username == #userId")
    public IsTokenValidDTO validateApiToken(@PathVariable String userId, @RequestBody ValidateTokenDTO validateTokenDTO) {
        boolean isValid = userService.isUserTokenIsValid(userId, validateTokenDTO.token());
        return new IsTokenValidDTO(isValid);
    }
}
