package cz.woidig.backend.service.user;

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
}
