package cz.woidig.backend.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUid(String uid);

    Optional<User> findUserByEmail(String email);
}
