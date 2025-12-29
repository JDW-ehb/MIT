package ehb.be.mit.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.rmi.server.UID;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "objectives")
public class Objective {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column( length = 100, nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    // e.g. TODO / IN_PROGRESS / DONE
    private String status;

    private LocalDate deadline;

    @ManyToMany(mappedBy = "objectives")
    @JsonIgnore   // prevents infinite JSON loop later
    private Set<User> users = new HashSet<>();

    public Objective() {}

    public Objective(String title, String description, String status, LocalDate deadline) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
    }

    // ----- Relationship helpers -----

    public void addUser(User user) {
        users.add(user);
        user.getObjectives().add(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.getObjectives().remove(this);
    }

    // ----- Getters & Setters -----

    public UUID getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public Set<User> getUsers() { return users; }
    public void setUsers(Set<User> users) { this.users = users; }



}
