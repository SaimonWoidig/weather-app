package cz.woidig.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String uid;
    private String email;
    private String hashedPassword;

    public User(String uid, String email, String hashedPassword) {
        this.uid = uid;
        this.email = email;
        this.hashedPassword = hashedPassword;
    }
}
