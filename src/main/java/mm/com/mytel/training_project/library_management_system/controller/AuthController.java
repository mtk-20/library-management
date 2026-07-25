package mm.com.mytel.training_project.library_management_system.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mm.com.mytel.training_project.library_management_system.common.constant.ErrorCode;
import mm.com.mytel.training_project.library_management_system.common.exception.CommonException;
import mm.com.mytel.training_project.library_management_system.common.exception.ResponseFactoryForException;
import mm.com.mytel.training_project.library_management_system.common.response.ResponseFactory;
import mm.com.mytel.training_project.library_management_system.common.util.JwtUtils;
import mm.com.mytel.training_project.library_management_system.common.util.TokenBlockUtil;
import mm.com.mytel.training_project.library_management_system.dto.request.LoginRequest;
import mm.com.mytel.training_project.library_management_system.dto.response.LoginResponse;
import mm.com.mytel.training_project.library_management_system.dto.response.UserResponse;
import mm.com.mytel.training_project.library_management_system.entity.Role;
import mm.com.mytel.training_project.library_management_system.entity.User;
import mm.com.mytel.training_project.library_management_system.enums.UserRoleName;
import mm.com.mytel.training_project.library_management_system.repo.RoleRepo;
import mm.com.mytel.training_project.library_management_system.repo.UserRepo;
import mm.com.mytel.training_project.library_management_system.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final UserService userService;
    private final ResponseFactory responseFactory;
    private final ResponseFactoryForException responseFactoryForException;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final TokenBlockUtil tokenBlockUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam UserRoleName roleName, @RequestParam String username, @RequestParam String password) {
        log.info("Registering user={}, role={}", username, roleName);
        UserResponse user = userService.register(roleName, username, password);

        return responseFactory.buildSuccess(HttpStatus.CREATED, user, ErrorCode.CREATED, "Registration success.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            log.info("Logging in user={}", request.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            String username = authentication.getName();
            User user = userRepo.findByUserName(username).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "User not found."));
            Role role = roleRepo.findById(user.getRoleId()).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Role not found."));

            String token = jwtUtils.generateToken(username);
            LoginResponse response = LoginResponse.builder()
                    .username(username)
                    .role(role.getUserRoleName().name())
                    .authenticated(true)
                    .message("Login success.")
                    .token(token)
                    .expiresIn(jwtUtils.getExpirationTimeInSeconds())
                    .build();

            return responseFactory.buildSuccess(
                    HttpStatus.OK,
                    response,
                    ErrorCode.OK,
                    response.getMessage()
            );
        } catch (BadCredentialsException e) {
            return responseFactoryForException.unauthorized(ErrorCode.UNAUTHORIZED, "Invalid username or password.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpRequest) {
        String token = jwtUtils.resolveBearerToken(httpRequest.getHeader("Authorization"));

        if (token == null || !jwtUtils.validateToken(token)) {
            return responseFactoryForException.unauthorized(ErrorCode.UNAUTHORIZED, "Valid authorization token is required.");
        }

        tokenBlockUtil.block(token, jwtUtils.extractExpiration(token));
        SecurityContextHolder.clearContext();

        log.info("User logged out and token revoked.");
        return responseFactory.buildSuccess(HttpStatus.OK, null, ErrorCode.OK, "Logout success.");
    }
}
