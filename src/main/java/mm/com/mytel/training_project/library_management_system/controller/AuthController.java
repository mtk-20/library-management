package mm.com.mytel.training_project.library_management_system.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mm.com.mytel.training_project.library_management_system.common.constant.ErrorCode;
import mm.com.mytel.training_project.library_management_system.common.exception.ResponseFactoryForException;
import mm.com.mytel.training_project.library_management_system.common.response.ResponseFactory;
import mm.com.mytel.training_project.library_management_system.common.util.JwtUtils;
import mm.com.mytel.training_project.library_management_system.dto.request.LoginRequest;
import mm.com.mytel.training_project.library_management_system.dto.response.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final ResponseFactory responseFactory;
    private final ResponseFactoryForException responseError;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            log.info("Logging in user={}", request.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            String username = authentication.getName();
            String token = jwtUtils.generateToken(username);
            LoginResponse response = LoginResponse.builder()
                    .username(username)
                    .authenticated(true)
                    .message("Login success.")
                    .token(token)
                    .expiresIn(jwtUtils.getExpirationTimeInSeconds())
                    .build();
            return responseFactory.buildSuccess(HttpStatus.OK, response, ErrorCode.OK, response.getMessage());
        } catch (BadCredentialsException e) {
            return responseError.unauthorized(ErrorCode.UNAUTHORIZED, "Invalid username or password.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpRequest) {

        log.info("Success logout.");
        return responseFactory.buildSuccess(HttpStatus.OK, null, ErrorCode.OK, "Logout success.");
    }
}
