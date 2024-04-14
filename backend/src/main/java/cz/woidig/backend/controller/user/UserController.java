package cz.woidig.backend.controller.user;

import cz.woidig.backend.dto.user.CreateUserDTO;
import cz.woidig.backend.dto.user.UserDTO;
import cz.woidig.backend.model.User;
import cz.woidig.backend.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user/create")
    public UserDTO createUser(@RequestBody CreateUserDTO createUserDTO) {
        User createdUser = userService.createUser(createUserDTO.email(), createUserDTO.password());
        return new UserDTO(createdUser.getUid(), createdUser.getEmail());
    }

    @GetMapping("/user")
    public List<UserDTO> getUsers() {
        return userService.getUsers().stream().map(user -> new UserDTO(user.getUid(), user.getEmail())).toList();
    }

    @GetMapping("/user/{uid}")
    public UserDTO getUser(@PathVariable String uid) {
        User user = userService.getUser(uid);
        return new UserDTO(user.getUid(), user.getEmail());
    }
}
