package ehb.be.mit.repositories;

import ehb.be.mit.models.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuditLogRepository extends JpaRepository<AuditLog, Long> {
}
