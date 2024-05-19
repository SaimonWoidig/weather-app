package cz.woidig.backend.controller.auth;

import cz.woidig.backend.dto.user.LoginUserDTO;
import cz.woidig.backend.dto.user.UserTokenDTO;
import cz.woidig.backend.model.User;
import cz.woidig.backend.service.user.JwtTokenService;
import cz.woidig.backend.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserTokenDTO> login(@RequestBody LoginUserDTO loginUserDTO) {
        User user = userService.getUserByEmailAndPassword(loginUserDTO.email(), loginUserDTO.password());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).build();
        }
        return ResponseEntity.ok(new UserTokenDTO(jwtTokenService.createUserJwt(user)));
    }
}
