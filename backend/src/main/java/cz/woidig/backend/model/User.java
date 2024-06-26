package cz.woidig.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity(name = "user")
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", unique = true, nullable = false, length = 21)
    private String userId;

    @Column(name = "password_hash", nullable = false, length = 128)
    @Setter
    private String passwordHash;

    @Column(name = "email", unique = true, nullable = false, length = 320)
    private String email;

    @Column(name = "api_token_hash", unique = true, nullable = true, length = 128)
    @Setter
    private String apiTokenHash;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<SavedLocation> savedLocations = new HashSet<>();

    public User(String userId, String email, String passwordHash) {
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash;
    }
}
