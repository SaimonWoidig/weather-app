package cz.woidig.backend.service.user;

import cz.woidig.backend.model.User;
import cz.woidig.backend.model.UserRepository;
import cz.woidig.backend.utils.id.IDGenerator;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private IDGenerator idGenerator;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User createUser(String email, String password) {
        String hashedPassword = passwordEncoder.encode(password);
        String uid = idGenerator.generateID();
        User user = new User(uid, email, hashedPassword);
        return createUser(user);
    }
}