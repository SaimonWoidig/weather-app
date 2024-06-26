package cz.woidig.backend.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUserId(String userId);

    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);
}
