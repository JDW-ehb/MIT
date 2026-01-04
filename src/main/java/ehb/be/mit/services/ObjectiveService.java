package ehb.be.mit.services;

import ehb.be.mit.models.Objective;
import ehb.be.mit.models.ObjectiveStatus;
import ehb.be.mit.models.ObjectiveType;
import ehb.be.mit.repositories.IObjectiveRepository;
import ehb.be.mit.repositories.IUserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ObjectiveService {

    private final IObjectiveRepository objectives;
    private final IUserRepository users;

    public ObjectiveService(IObjectiveRepository objectives, IUserRepository users) {
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

    public long countTodayMITsForUser(UUID userId) {
        return objectives.countByUsers_IdAndCreatedAtAndType(
                userId,
                LocalDate.now(),
                ObjectiveType.MIT
        );
    }

    public void save(Objective obj){
        objectives.save(obj);
    }

    public void createObjectiveForUser(
            UUID userId,
            String title,
            String description,
            ObjectiveStatus status
    ) {
        var user = users.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        long already = countTodayMITsForUser(userId);
        if (already >= 3)
            throw new IllegalStateException("You already created today's 3 MITs 🤖");

        var today = LocalDate.now();

        var obj = new Objective();
        obj.setTitle(title);
        obj.setDescription(description);
        obj.setCreatedAt(today);
        obj.setDeadline(today); // ok — you can later drop this field if you want
        obj.setStatus(status);
        obj.setType(ObjectiveType.MIT);

        objectives.save(obj);

        user.addObjective(obj);
        users.save(user);
    }
}
