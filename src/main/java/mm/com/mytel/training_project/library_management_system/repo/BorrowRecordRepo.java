package mm.com.mytel.training_project.library_management_system.repo;

import mm.com.mytel.training_project.library_management_system.dto.response.MostBorrowedBooksResponse;
import mm.com.mytel.training_project.library_management_system.entity.BorrowRecord;
import mm.com.mytel.training_project.library_management_system.enums.BorrowStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BorrowRecordRepo extends JpaRepository<BorrowRecord, Long> {

    boolean existsByMemberIdAndBookIdAndStatus(Long memberId, Long bookId, BorrowStatus status);

    Optional<BorrowRecord> findByIdAndMemberId(Long id, Long memberId);

    Page<BorrowRecord> findByMemberIdAndStatus(Long memberId, BorrowStatus status, Pageable pageable);

    @Query("""
            SELECT b
            FROM BorrowRecord b
            WHERE b.memberId = :memberId
            AND b.status = BorrowStatus.BORROWED
            ORDER BY b.dueDate ASC
            """)
    Page<BorrowRecord> findByDueDate(@Param("memberId") Long memberId, Pageable pageable);

   /*
   @Query("""
            SELECT COUNT(br)
            FROM BorrowRecord br
            WHERE br.status IN ('BORROWED','OVERDUE')
            """)
    Long countCurrentBorrowedBooks();
    */

    Page<BorrowRecord> findByStatus(BorrowStatus status, Pageable pageable);

    @Query(value = """
                SELECT 
                    b.id AS bookId, 
                    b.isbn AS isbn, 
                    b.title AS title, 
                    b.publisher AS publisher, 
                    COUNT(br.id) AS totalBorrowCount 
                FROM BorrowRecord br 
                JOIN Book b ON br.bookId = b.id 
                GROUP BY b.id, b.isbn, b.title, b.publisher 
                ORDER BY COUNT(br.id) DESC
            """,
            countQuery = "SELECT COUNT(DISTINCT br.bookId) FROM BorrowRecord br")
    Page<MostBorrowedBooksResponse> findMostBorrowedBooks(Pageable pageable);
}
