package cz.woidig.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "saved_location")
@Table(name = "saved_locations")
@NoArgsConstructor
public class SavedLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "location_id", unique = true, nullable = false, length = 21)
    private String locationId;

    @Column(name = "name", nullable = false, length = 128)
    private String name;

    @Column(name = "latitude", nullable = false)
    private float latitude;

    @Column(name = "longitude", nullable = false)
    private float longitude;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public SavedLocation(String locationId, String name, float latitude, float longitude, User user) {
        this.locationId = locationId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
    }
}
