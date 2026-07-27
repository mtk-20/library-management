package mm.com.mytel.training_project.library_management_system.service;

import mm.com.mytel.training_project.library_management_system.common.response.Basic;
import mm.com.mytel.training_project.library_management_system.dto.request.MemberRequest;
import mm.com.mytel.training_project.library_management_system.dto.request.MemberUpdateRequest;
import org.springframework.http.ResponseEntity;

public interface MemberService {

    ResponseEntity<Basic> registerMember(MemberRequest memberRequest);

    ResponseEntity<Basic> updateMember(Long id, MemberUpdateRequest memberUpdateRequest);

    ResponseEntity<Basic> deleteMember(Long id);

    ResponseEntity<Basic> listAllMembers(int page, int size);

    ResponseEntity<?> getMemberById(Long id);
}
