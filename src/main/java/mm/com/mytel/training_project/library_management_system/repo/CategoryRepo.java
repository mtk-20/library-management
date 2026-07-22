package mm.com.mytel.training_project.library_management_system.repo;

import mm.com.mytel.training_project.library_management_system.entity.Book;
import mm.com.mytel.training_project.library_management_system.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    boolean existsByCategoryName(String categoryName);

    Page<Category> findAllByOrderByCategoryNameAsc(Pageable pageable);
}
