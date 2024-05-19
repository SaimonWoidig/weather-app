package cz.woidig.backend.service.user;

import cz.woidig.backend.model.User;
import cz.woidig.backend.model.UserRepository;
import cz.woidig.backend.utils.id.IDGenerator;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
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

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(String uid) {
        return userRepository.findUserByUid(uid).orElseThrow(() -> new NoSuchElementException("No user with uid " + uid));
    }

    public User getUserByEmailAndPassword(String email, String password) {
        User user = userRepository.findUserByEmail(email).orElse(null);
        if (user == null) {
            return null;
        }
        if (!passwordEncoder.matches(password, user.getHashedPassword())) {
            return null;
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUid(username).orElseThrow(() -> new UsernameNotFoundException("User not found with email " + username));
        return UserPrincipal.createFromUser(user);
    }
}
