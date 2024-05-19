package cz.woidig.backend.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity(name = "user")
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", unique = true, nullable = false, length = 21)
    private String userId;

    @Column(name = "password_hash", nullable = false, length = 128)
    private String passwordHash;

    @Column(name = "email", unique = true, nullable = false, length = 320)
    private String email;
}
