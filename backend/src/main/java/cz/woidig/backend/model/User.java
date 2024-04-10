package cz.woidig.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Getter
    private String uid;
    @Getter
    private String email;
    @Getter
    private String hashedPassword;

    public User(String uid, String email, String hashedPassword) {
        this.uid = uid;
        this.email = email;
        this.hashedPassword = hashedPassword;
    }
}
