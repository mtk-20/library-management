package mm.com.mytel.training_project.library_management_system.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mm.com.mytel.training_project.library_management_system.common.constant.ErrorCode;
import mm.com.mytel.training_project.library_management_system.common.exception.CommonException;
import mm.com.mytel.training_project.library_management_system.common.exception.ResponseFactoryForException;
import mm.com.mytel.training_project.library_management_system.common.response.Basic;
import mm.com.mytel.training_project.library_management_system.common.response.ResponseFactory;
import mm.com.mytel.training_project.library_management_system.dto.request.MemberRequest;
import mm.com.mytel.training_project.library_management_system.dto.request.MemberUpdateRequest;
import mm.com.mytel.training_project.library_management_system.dto.response.MemberResponse;
import mm.com.mytel.training_project.library_management_system.entity.Member;
import mm.com.mytel.training_project.library_management_system.entity.Role;
import mm.com.mytel.training_project.library_management_system.enums.MemberStatus;
import mm.com.mytel.training_project.library_management_system.enums.UserRoleName;
import mm.com.mytel.training_project.library_management_system.repo.MemberRepo;
import mm.com.mytel.training_project.library_management_system.repo.RoleRepo;
import mm.com.mytel.training_project.library_management_system.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepo memberRepo;
    private final RoleRepo roleRepo;
    private final ResponseFactory responseFactory;
    private final ResponseFactoryForException responseFactoryForException;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");

    @Override
    public ResponseEntity<Basic> registerMember(MemberRequest memberRequest) {
            UserRoleName role = UserRoleName.MEMBER;
            Role memberRole = roleRepo.findByUserRoleName(role).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Member role not found."));

            Member entity = new Member();
            if (memberRepo.existsByMemberName(memberRequest.getMemberName())) {
                throw new CommonException(ErrorCode.DUPLICATE, "Username already registered.");
            }
            entity.setMemberName(memberRequest.getMemberName());
            if (memberRepo.existsByEmail(memberRequest.getEmail())) {
                throw new CommonException(ErrorCode.DUPLICATE, "Email already registered.");
            }
            entity.setEmail(memberRequest.getEmail());
            if (memberRepo.existsByPhoneNumber(memberRequest.getPhoneNumber())) {
                throw new CommonException(ErrorCode.DUPLICATE, "Phone Number already registered.");
            }
            entity.setPhoneNumber(memberRequest.getPhoneNumber());
            entity.setAddress(memberRequest.getAddress());
            entity.setRegisterTime((LocalDateTime.now()));
            entity.setMemberStatus(MemberStatus.ACTIVE);
            entity.setRoleId(memberRole.getId());
            memberRepo.save(entity);

            MemberResponse response = new MemberResponse();
            response.setMemberName(entity.getMemberName());
            response.setEmail(entity.getEmail());
            response.setPhoneNumber(entity.getPhoneNumber());
            response.setAddress(entity.getAddress());
            response.setRegisterTime(entity.getRegisterTime().format(formatter));
            response.setMemberStatus(entity.getMemberStatus());

            return responseFactory.buildSuccess(
                    HttpStatus.CREATED,
                    response,
                    ErrorCode.CREATED,
                    "Member register success."
            );
    }

    @Override
    public ResponseEntity<Basic> updateMember(Long id, MemberUpdateRequest memberUpdateRequest) {
        Member entity = memberRepo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Member not found."));
        if (memberUpdateRequest.getMemberName() != null) {
            entity.setMemberName(memberUpdateRequest.getMemberName());
        }
        if (memberUpdateRequest.getEmail() != null) {
            entity.setEmail(memberUpdateRequest.getEmail());
        }
        if (memberUpdateRequest.getPhoneNumber() != null) {
            entity.setPhoneNumber(memberUpdateRequest.getPhoneNumber());
        }
        if (memberUpdateRequest.getAddress() != null) {
            entity.setAddress(memberUpdateRequest.getAddress());
        }
        memberRepo.save(entity);

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                entity,
                ErrorCode.OK,
                "Member update success."
        );
    }

    @Override
    public ResponseEntity<Basic> deleteMember(Long id) {
        MemberStatus setToInactive = MemberStatus.INACTIVE;
        Member entity = memberRepo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Member not found."));
        entity.setMemberStatus(setToInactive);
        memberRepo.save(entity);

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                null,
                ErrorCode.OK,
                "Member delete success."
        );
    }

    @Override
    public ResponseEntity<Basic> listAllMembers(int page, int size) {
        MemberStatus status = MemberStatus.ACTIVE;
        Pageable pageable = PageRequest.of(page, size);
        Page<Member> entities = memberRepo.findByMemberStatusOrderByRegisterTimeAsc(status, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("members", entities);

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                response,
                ErrorCode.OK,
                "Member list request success."
        );
    }
}
