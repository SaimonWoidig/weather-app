package cz.woidig.backend.service.user;

import cz.woidig.backend.model.User;
import cz.woidig.backend.model.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApiTokenService {
    private final UserRepository userRepository;

    public String getUserApiToken(String email) {
        User user = userRepository.findUserByEmail(email).orElse(null);
        if (user == null) {
            return null;
        }
        return user.getApiToken();
    }

    public User getUserFromApiToken(String apiToken) {
        return userRepository.findUserByApiToken(apiToken).orElse(null);
    }
}
