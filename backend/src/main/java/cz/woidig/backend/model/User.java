package cz.woidig.backend.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Table(name = "users")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter(AccessLevel.NONE)
    private Long id;
    private String uid;
    private String email;
    private String hashedPassword;
    @Nullable
    @Setter
    private String apiToken;

    public User(String uid, String email, String hashedPassword) {
        this.uid = uid;
        this.email = email;
        this.hashedPassword = hashedPassword;
    }
}
