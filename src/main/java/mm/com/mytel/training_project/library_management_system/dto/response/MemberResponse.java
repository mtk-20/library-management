package mm.com.mytel.training_project.library_management_system.dto.response;

import lombok.Data;
import mm.com.mytel.training_project.library_management_system.enums.MemberStatus;

@Data
public class MemberResponse {

    private String memberName;
    private String email;
    private String phoneNumber;
    private String address;
    private String registerTime;
    private MemberStatus memberStatus;
}
