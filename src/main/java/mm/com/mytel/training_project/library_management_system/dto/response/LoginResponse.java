package mm.com.mytel.training_project.library_management_system.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private String username;
    private String message;
    private boolean authenticated;
    private String token;
    private Long expiresIn;
}
