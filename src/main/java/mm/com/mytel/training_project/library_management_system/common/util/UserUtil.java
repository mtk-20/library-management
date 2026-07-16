package mm.com.mytel.training_project.library_management_system.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {

    public String getCurrentLoginUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public String getCurrentUserRole() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);
    }


    public String extractVmyCode(String principal) {
        return principal.contains("|")
                ? principal.split("\\|")[0]
                : principal;
    }

    public String extractUserName(String principal) {
        return principal.contains("|")
                ? principal.split("\\|")[1]
                : principal;
    }
}
