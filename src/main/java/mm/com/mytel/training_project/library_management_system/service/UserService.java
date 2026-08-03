package mm.com.mytel.training_project.library_management_system.service;

import mm.com.mytel.training_project.library_management_system.common.response.Basic;
import mm.com.mytel.training_project.library_management_system.dto.request.UserUpdateRequest;
import mm.com.mytel.training_project.library_management_system.dto.response.UserResponse;
import mm.com.mytel.training_project.library_management_system.enums.UserRoleName;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<Basic> getAllUsers();

    UserResponse register(UserRoleName roleName, String username, String password);

    UserResponse update(Long id, UserUpdateRequest updateRequest);
}
