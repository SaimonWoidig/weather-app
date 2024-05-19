package cz.woidig.backend.model;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface SavedLocationRepository extends JpaRepository<SavedLocation, Integer> {
    Set<SavedLocation> findAllByUser_UserId(String userId);

    SavedLocation findByLocationId(String locationId);

    @Transactional
    void deleteByLocationId(String locationId);
}
