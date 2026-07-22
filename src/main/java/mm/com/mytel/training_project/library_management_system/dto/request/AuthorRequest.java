package mm.com.mytel.training_project.library_management_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthorRequest {

    @NotBlank
    private String authorName;

    @NotBlank
    private String biography;

    @NotBlank
    private String nationality;
}