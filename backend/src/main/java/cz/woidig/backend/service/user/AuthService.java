package cz.woidig.backend.service.user;

import cz.woidig.backend.dto.user.UserDTO;
import cz.woidig.backend.exceptions.InvalidPasswordException;
import cz.woidig.backend.exceptions.UserAlreadyExistsException;
import cz.woidig.backend.model.User;
import cz.woidig.backend.model.UserRepository;
import cz.woidig.backend.service.id.IdGenerationService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdGenerationService idGenerationService;
    private final JwtTokenService jwtTokenService;

    public UserDTO registerUser(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User with email '" + email + "' already exists");
        }

        String hashedPassword = passwordEncoder.encode(password);
        String userId = idGenerationService.generateId();
        String jwtToken = jwtTokenService.createUserJwt(userId);
        User user = new User(userId, email, hashedPassword);
        userRepository.save(user);

        return new UserDTO(email, userId, jwtToken);
    }

    public UserDTO loginUser(String email, String password) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email '" + email + "' not found"));
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new InvalidPasswordException("Invalid password for user with email '" + email + "'");
        }

        String jwtToken = jwtTokenService.createUserJwt(user.getUserId());

        return new UserDTO(user.getEmail(), user.getUserId(), jwtToken);
    }
}
