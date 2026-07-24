package mm.com.mytel.training_project.library_management_system.controller;

import lombok.RequiredArgsConstructor;
import mm.com.mytel.training_project.library_management_system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<?> handleGetAllUsers() {
        return userService.getAllUsers();
    }
}
