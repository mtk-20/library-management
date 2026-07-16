package mm.com.mytel.training_project.library_management_system.service;

import mm.com.mytel.training_project.library_management_system.common.response.Basic;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<Basic> getAllUsers();
}
