package mm.com.mytel.training_project.library_management_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthorUpdateRequest {

    @NotBlank
    private String biography;
}
