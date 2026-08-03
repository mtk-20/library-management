package mm.com.mytel.training_project.library_management_system.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mm.com.mytel.training_project.library_management_system.common.constant.ErrorCode;
import mm.com.mytel.training_project.library_management_system.common.response.ResponseFactory;
import mm.com.mytel.training_project.library_management_system.dto.request.UserUpdateRequest;
import mm.com.mytel.training_project.library_management_system.dto.response.UserResponse;
import mm.com.mytel.training_project.library_management_system.enums.UserRoleName;
import mm.com.mytel.training_project.library_management_system.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final ResponseFactory responseFactory;

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @GetMapping()
    public ResponseEntity<?> handleGetAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam UserRoleName roleName, @RequestParam String username, @RequestParam String password) {
        log.info("Registering user={}, role={}", username, roleName);
        UserResponse user = userService.register(roleName, username, password);

        return responseFactory.buildSuccess(HttpStatus.CREATED, user, ErrorCode.CREATED, "Registration success.");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/update")
    public ResponseEntity<?> update(@RequestParam Long id, @RequestBody UserUpdateRequest updateRequest) {
        log.info("Updating userId={}", id);
        UserResponse user = userService.update(id, updateRequest);

        return responseFactory.buildSuccess(HttpStatus.OK, user, ErrorCode.OK, "Update success.");
    }
}
