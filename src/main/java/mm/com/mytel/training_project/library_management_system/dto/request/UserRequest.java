package mm.com.mytel.training_project.library_management_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserRequest {

    @NotBlank
    private String name;

    @NonNull
    private Long roleId;
}
