package ehb.be.mit.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "AuditLogs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String event;

    private LocalDateTime timestamp = LocalDateTime.now();

    @ManyToOne
    private User user;

    public AuditLog() {}

    public AuditLog(String event, User user) {
        this.event = event;
        this.user = user;
    }

    public Long getId() { return id; }

    public String getEvent() { return event; }
    public void setEvent(String event) { this.event = event; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

}
