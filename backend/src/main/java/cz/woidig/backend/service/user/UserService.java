package cz.woidig.backend.service.user;

import cz.woidig.backend.dto.user.NewApiTokenDTO;
import cz.woidig.backend.model.User;
import cz.woidig.backend.model.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGeneratorService tokenGeneratorService;

    public User getUser(String userId) {
        return userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User with id '" + userId + "' not found"));
    }

    public void changeUserPassword(String userId, String newPassword) {
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User with id '" + userId + "' not found"));
        String newHashedPassword = passwordEncoder.encode(newPassword);
        user.setPasswordHash(newHashedPassword);
        userRepository.save(user);
    }

    public NewApiTokenDTO newApiToken(String userId) {
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User with id '" + userId + "' not found"));
        String token = tokenGeneratorService.generateToken();
        String hashedToken = passwordEncoder.encode(token);
        user.setApiTokenHash(hashedToken);
        userRepository.save(user);
        return new NewApiTokenDTO(token);
    }

    public boolean isUserTokenIsValid(String userId, String token) {
        if (token == null) {
            return false;
        }
        try {
            User user = userRepository.findUserByUserId(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User with id '" + userId + "' not found"));
            String hashedToken = user.getApiTokenHash();
            return passwordEncoder.matches(token, hashedToken);
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }
}
