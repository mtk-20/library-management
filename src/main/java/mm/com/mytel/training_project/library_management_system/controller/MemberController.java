package mm.com.mytel.training_project.library_management_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mm.com.mytel.training_project.library_management_system.dto.request.MemberRequest;
import mm.com.mytel.training_project.library_management_system.dto.request.MemberUpdateRequest;
import mm.com.mytel.training_project.library_management_system.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @PostMapping()
    public ResponseEntity<?> handleRegisterMember(@Valid @RequestBody MemberRequest memberRequest) {
        return memberService.registerMember(memberRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'MEMBER')")
    @PatchMapping()
    public ResponseEntity<?> handleUpdateMember(@RequestParam Long id, @Valid @RequestBody MemberUpdateRequest memberUpdateRequest) {
        return memberService.updateMember(id, memberUpdateRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping()
    public ResponseEntity<?> handleDeleteMember(@RequestParam Long id) {
        return memberService.deleteMember(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @GetMapping()
    public ResponseEntity<?> handleListAllMembers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return memberService.listAllMembers(page, size);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @GetMapping("/id")
    public ResponseEntity<?> handleGetMemberById(@RequestParam Long id) {
        return memberService.getMemberById(id);
    }
}
