package cz.woidig.backend.controller.user;

import cz.woidig.backend.dto.user.LoginDTO;
import cz.woidig.backend.dto.user.RegisterUserDTO;
import cz.woidig.backend.dto.user.UserDTO;
import cz.woidig.backend.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public UserDTO register(@RequestBody RegisterUserDTO registerUserDTO) {
        return userService.registerUser(registerUserDTO.email(), registerUserDTO.password());
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody LoginDTO registerUserDTO) {
        return userService.loginUser(registerUserDTO.email(), registerUserDTO.password());
    }
}
