package mm.com.mytel.training_project.library_management_system.repo;

import mm.com.mytel.training_project.library_management_system.entity.Member;
import mm.com.mytel.training_project.library_management_system.enums.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepo extends JpaRepository<Member, Long> {

    Page<Member> findByMemberStatusOrderByRegisterTimeAsc(MemberStatus memberStatus, Pageable pageable);

    Optional<Member> findByUserId(Long userId);

    boolean existsByMemberName(String memberName);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}
