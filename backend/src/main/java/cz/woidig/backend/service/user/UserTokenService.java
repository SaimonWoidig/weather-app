package cz.woidig.backend.service.user;

import cz.woidig.backend.model.User;
import cz.woidig.backend.model.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserTokenService {
    private final UserRepository userRepository;

    public String getUserAppToken(String email) {
        User user = userRepository.findUserByEmail(email).orElse(null);
        if (user == null) {
            return null;
        }
        return user.getAppToken();
    }

    public UserPrincipal getUserFromAppToken(String appToken) {
        User user = userRepository.findUserByAppToken(appToken).orElse(null);
        if (user == null) {
            return null;
        }
        return UserPrincipal.createFromUser(user);
    }
}
