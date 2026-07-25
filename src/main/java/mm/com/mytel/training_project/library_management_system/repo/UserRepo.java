package mm.com.mytel.training_project.library_management_system.repo;

import mm.com.mytel.training_project.library_management_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    boolean existsByUserName(String username);
}
