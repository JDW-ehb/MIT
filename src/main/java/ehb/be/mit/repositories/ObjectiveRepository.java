package ehb.be.mit.repositories;

import ehb.be.mit.models.Objective;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ObjectiveRepository extends JpaRepository<Objective, UUID> {
    long countByUsers_IdAndCreatedAt(UUID userId, LocalDate date);
    List<Objective> findByUsers_Id(UUID userId);
    List<Objective> findByUsers_IdAndCreatedAt(UUID userId, LocalDate createdAt);

}