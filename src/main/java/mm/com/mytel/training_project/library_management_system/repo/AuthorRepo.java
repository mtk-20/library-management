package mm.com.mytel.training_project.library_management_system.repo;

import mm.com.mytel.training_project.library_management_system.entity.Author;
import mm.com.mytel.training_project.library_management_system.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthorRepo extends JpaRepository<Author, Long> {

    boolean existsByAuthorName(String authorName);

    @Query("SELECT a FROM Author a ORDER BY a.authorName ASC")
    Page<Author> findAllAuthors(Pageable pageable);
}
