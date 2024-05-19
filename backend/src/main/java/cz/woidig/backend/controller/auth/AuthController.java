package cz.woidig.backend.controller.auth;

import cz.woidig.backend.dto.user.LoginDTO;
import cz.woidig.backend.dto.user.RegisterUserDTO;
import cz.woidig.backend.dto.user.UserDTO;
import cz.woidig.backend.service.user.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public UserDTO register(@RequestBody RegisterUserDTO registerUserDTO) {
        return authService.registerUser(registerUserDTO.email(), registerUserDTO.password());
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody LoginDTO registerUserDTO) {
        return authService.loginUser(registerUserDTO.email(), registerUserDTO.password());
    }
}
