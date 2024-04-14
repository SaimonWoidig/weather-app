package cz.woidig.backend.dto.user;

public record CreateUserDTO(
        String email,
        String password
) {
}
