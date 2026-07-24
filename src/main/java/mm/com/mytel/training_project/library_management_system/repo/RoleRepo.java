package mm.com.mytel.training_project.library_management_system.repo;

import mm.com.mytel.training_project.library_management_system.entity.Role;
import mm.com.mytel.training_project.library_management_system.enums.UserRoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {

    Optional<Role> findByUserRoleName(UserRoleName userRoleName);
}
