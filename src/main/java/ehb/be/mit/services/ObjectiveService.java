package ehb.be.mit.services;

import ehb.be.mit.models.Objective;
import ehb.be.mit.models.ObjectiveStatus;
import ehb.be.mit.repositories.ObjectiveRepository;
import ehb.be.mit.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ObjectiveService {

    private final ObjectiveRepository objectives;
    private final UserRepository users;

    public ObjectiveService(ObjectiveRepository objectives, UserRepository users) {
        this.objectives = objectives;
        this.users = users;
    }

    public Objective getById(UUID id){
        return objectives.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Objective not found"));
    }

    public List<Objective> getAllForUserAndDate(UUID userId, LocalDate date){
        return objectives.findByUsers_IdAndCreatedAt(userId, date);
    }

    public long countTodayForUser(UUID userId) {
        return objectives.countByUsers_IdAndCreatedAt(
                userId,
                LocalDate.now()
        );
    }

    public void save(Objective obj){
        objectives.save(obj);
    }

    public void createObjectiveForUser(
            UUID userId,
            String title,
            String description,
            LocalDate deadline
    ) {
        var user = users.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var today = LocalDate.now();

        long already = countTodayForUser(userId);

        if (already >= 3)
            throw new IllegalStateException("You already created today's 3 MITs 🤖");

        var obj = new Objective();
        obj.setTitle(title);
        obj.setDescription(description);
        obj.setDeadline(deadline);
        obj.setCreatedAt(today);
        obj.setStatus(ObjectiveStatus.IN_PROGRESS);

        objectives.save(obj);

        user.addObjective(obj);
        users.save(user);
    }
}

