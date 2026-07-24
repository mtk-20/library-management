package mm.com.mytel.training_project.library_management_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MemberRequest {

    @NotBlank
    private String memberName;

    @NotBlank
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank
    @Pattern(regexp = "^(09|959)\\d{6,9}$", message = "prefix 09 or 959 and suffix between 6 and 9.")
    private String phoneNumber;

    @NotBlank
    private String address;
}
