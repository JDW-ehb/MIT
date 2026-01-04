package ehb.be.mit.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user_groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 60)
    private String name;

    @Column(length = 140)
    private String shortDescription;

    @Column(length = 400)
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "group")
    private Set<Objective> objectives = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();

    public Group(){}

    // --- RELATIONSHIP HELPERS ---

    public void addMember(User u){
        members.add(u);
        u.getGroups().add(this);
    }

    public void removeMember(User u){
        members.remove(u);
        u.getGroups().remove(this);
    }


    // --- GETTERS & SETTERS ---

    public UUID getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public Set<Objective> getObjectives() {return objectives;}
    public void setObjectives(Set<Objective> objectives) {this.objectives = objectives;}


    public Set<User> getMembers() { return members; }
    public void setMembers(Set<User> members) { this.members = members; }


}
