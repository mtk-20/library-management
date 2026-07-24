package mm.com.mytel.training_project.library_management_system.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mm.com.mytel.training_project.library_management_system.common.constant.ErrorCode;
import mm.com.mytel.training_project.library_management_system.common.exception.CommonException;
import mm.com.mytel.training_project.library_management_system.common.response.Basic;
import mm.com.mytel.training_project.library_management_system.common.response.ResponseFactory;
import mm.com.mytel.training_project.library_management_system.dto.response.UserResponse;
import mm.com.mytel.training_project.library_management_system.entity.Role;
import mm.com.mytel.training_project.library_management_system.entity.User;
import mm.com.mytel.training_project.library_management_system.repo.RoleRepo;
import mm.com.mytel.training_project.library_management_system.repo.UserRepo;
import mm.com.mytel.training_project.library_management_system.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final ResponseFactory responseFactory;

    @Override
    public ResponseEntity<Basic> getAllUsers() {

        List<User> users = userRepo.findAll();

        List<UserResponse> response = users.stream().map(user -> {
            Role role = roleRepo.findById(user.getRoleId()).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Role not found"));
            UserResponse dto = new UserResponse();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setRoleId(user.getRoleId());
            dto.setRoleName(role.getUserRoleName().name());

            return dto;
        }).toList();

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                response,
                ErrorCode.OK,
                "Get User list Success."
        );
    }
}
