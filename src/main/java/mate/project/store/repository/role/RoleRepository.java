package mate.project.store.repository.role;

import java.util.Optional;
import mate.project.store.entity.Role;
import mate.project.store.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    Optional<Role> findByRoleName(RoleName roleName);
}
