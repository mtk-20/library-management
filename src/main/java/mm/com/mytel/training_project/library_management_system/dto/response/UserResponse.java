package mm.com.mytel.training_project.library_management_system.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private Long roleId;
    private String roleName;
}
