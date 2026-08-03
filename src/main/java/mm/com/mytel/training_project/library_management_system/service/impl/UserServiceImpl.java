package mm.com.mytel.training_project.library_management_system.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mm.com.mytel.training_project.library_management_system.common.constant.ErrorCode;
import mm.com.mytel.training_project.library_management_system.common.exception.CommonException;
import mm.com.mytel.training_project.library_management_system.common.response.Basic;
import mm.com.mytel.training_project.library_management_system.common.response.ResponseFactory;
import mm.com.mytel.training_project.library_management_system.dto.request.UserUpdateRequest;
import mm.com.mytel.training_project.library_management_system.dto.response.UserResponse;
import mm.com.mytel.training_project.library_management_system.entity.Role;
import mm.com.mytel.training_project.library_management_system.entity.User;
import mm.com.mytel.training_project.library_management_system.enums.UserRoleName;
import mm.com.mytel.training_project.library_management_system.repo.RoleRepo;
import mm.com.mytel.training_project.library_management_system.repo.UserRepo;
import mm.com.mytel.training_project.library_management_system.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final ResponseFactory responseFactory;

    @Override
    public ResponseEntity<Basic> getAllUsers() {

        List<UserResponse> users = userRepo.findAll().stream()
                .map(this::userResponse)
                .toList();

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                users,
                ErrorCode.OK,
                "Get User list Success."
        );
    }

    @Override
    public UserResponse register(UserRoleName roleName, String username, String rawPassword) {
        return userResponse(createUser(username, rawPassword, roleName));
    }

    @Transactional
    public User createUser(String username, String rawPassword, UserRoleName roleName) {
        if (userRepo.existsByUserName(username)) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "Username already exists.");
        }

        Role role = roleRepo.findByUserRoleName(roleName).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Role not found."));

        User user = User.builder()
                .userName(username)
                .password(passwordEncoder.encode(rawPassword))
                .roleId(role.getId())
                .build();
        return userRepo.save(user);
    }

    @Override
    public UserResponse update(Long id, UserUpdateRequest updateRequest) {
        return userResponse(updateUser(id, updateRequest));
    }

    @Transactional
    public User updateUser(Long id, UserUpdateRequest updateRequest) {
        User user = userRepo.findById(id).orElseThrow(()-> new CommonException(ErrorCode.NOT_FOUND, "User not found."));
        Role role = roleRepo.findById(user.getRoleId()).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Role not found."));
        if (role.getUserRoleName() != UserRoleName.LIBRARIAN) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "This user is not a librarian.");
        }
        if (userRepo.existsByUserName(updateRequest.getUsername())) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "Username already exists.");
        }
        user.setUserName(updateRequest.getUsername());
        user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));

        return userRepo.save(user);
    }

    private UserResponse userResponse(User user) {

        Role role = roleRepo.findById(user.getRoleId()).orElseThrow(()-> new CommonException(ErrorCode.NOT_FOUND, "Role not found."));

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getUserName())
                .roleId(user.getRoleId())
                .roleName(role.getUserRoleName().name())
                .build();
    }
}
