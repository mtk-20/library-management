package mm.com.mytel.training_project.library_management_system.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mm.com.mytel.training_project.library_management_system.entity.Role;
import mm.com.mytel.training_project.library_management_system.entity.User;
import mm.com.mytel.training_project.library_management_system.repo.RoleRepo;
import mm.com.mytel.training_project.library_management_system.repo.UserRepo;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("User not found."));
        Role role = roleRepo.findById(user.getRoleId()).orElseThrow(() -> new UsernameNotFoundException("Role not found."));

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .authorities("ROLE_" + role.getUserRoleName().name())
                .build();
    }
}
