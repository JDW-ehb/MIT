package ehb.be.mit.models;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 40)
    private String username;

    @Column(unique = true, length = 120)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String role;  // USER / ADMIN later

    // --- NEW FIELD ---
    @Column(length = 2)
    private String country;   // ISO-2 like "BE", "FR", "NL"

    @ManyToMany
    @JoinTable(
            name = "user_objectives",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "objective_id")
    )
    private Set<Objective> objectives = new HashSet<>();

    public User() {}

    public User(String username, String email, String passwordHash, String role) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    // relationship helpers
    public void addObjective(Objective objective) {
        objectives.add(objective);
        objective.getUsers().add(this);
    }

    public void removeObjective(Objective objective) {
        objectives.remove(objective);
        objective.getUsers().remove(this);
    }

    // getters & setters

    public UUID getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Set<Objective> getObjectives() { return objectives; }
    public void setObjectives(Set<Objective> objectives) { this.objectives = objectives; }
}

