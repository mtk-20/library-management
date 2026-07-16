package mm.com.mytel.training_project.library_management_system.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {

    @NotBlank
    private String name;

    @NotBlank
    private String role;
}
