package ehb.be.mit.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ObjectiveStatus status = ObjectiveStatus.IN_PROGRESS;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ObjectiveType type = ObjectiveType.SUBOBJECTIVE;


    private LocalDate deadline;

    private LocalDate createdAt = LocalDate.now();

    @ManyToMany(mappedBy = "objectives")
    @JsonIgnore   // prevents infinite JSON loop later
    private Set<User> users = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;


    public Objective() {}

    public Objective(String title, String description, ObjectiveStatus status, LocalDate deadline) {
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

    public ObjectiveStatus getStatus() { return status; }
    public void setStatus(ObjectiveStatus status) {this.status = status;}
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public Set<User> getUsers() { return users; }
    public void setUsers(Set<User> users) { this.users = users; }

    public ObjectiveType getType() { return type; }
    public void setType(ObjectiveType type) { this.type = type; }


    public Group getGroup() {return group;}
    public void setGroup(Group group) {this.group = group;}

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }


}
