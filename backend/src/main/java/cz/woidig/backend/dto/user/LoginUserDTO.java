package cz.woidig.backend.dto.user;

public record LoginUserDTO(
        String email,
        String password
) {
}
