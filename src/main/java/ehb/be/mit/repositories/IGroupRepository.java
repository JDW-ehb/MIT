package ehb.be.mit.repositories;

import ehb.be.mit.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IGroupRepository extends JpaRepository<Group, UUID> {
    List<Group> findByMembersUsername(String username);
}
