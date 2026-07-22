package mm.com.mytel.training_project.library_management_system.repo;

import mm.com.mytel.training_project.library_management_system.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {

    @Query("""
            SELECT b
            FROM Book b
            WHERE (:isbn IS NULL OR b.isbn LIKE LOWER(CONCAT('%', :isbn, '%')))
              AND (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))
              AND (:authorId IS NULL OR b.authorId = :authorId)
              AND (:categoryId IS NULL OR b.categoryId = :categoryId)
              ORDER BY b.publicationYear DESC, b.id ASC
            """)
    Page<Book> filterBooks(@Param("isbn") String isbn,
                           @Param("title") String title,
                           @Param("authorId") Long authorId,
                           @Param("categoryId") Long categoryId,
                           Pageable pageable
    );

    Page<Book> findAllByOrderByPublicationYearDescIdAsc(Pageable pageable);

    boolean existsByAuthorId(Long id);
}
