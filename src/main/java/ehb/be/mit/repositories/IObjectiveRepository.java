package ehb.be.mit.repositories;

import ehb.be.mit.models.Objective;
import ehb.be.mit.models.ObjectiveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IObjectiveRepository extends JpaRepository<Objective, UUID> {
    List<Objective> findByUsers_IdAndCreatedAt(UUID userId, LocalDate createdAt);

    long countByUsers_IdAndCreatedAtAndType(UUID userId, LocalDate date, ObjectiveType type);


    List<Objective> findByTypeNot(ObjectiveType type);


}