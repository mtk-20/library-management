package mm.com.mytel.training_project.library_management_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberUpdateRequest {

    private String memberName;
    private String email;
    private String phoneNumber;
    private String address;
}
