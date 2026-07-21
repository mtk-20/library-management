package mm.com.mytel.training_project.library_management_system.service;

import mm.com.mytel.training_project.library_management_system.common.response.Basic;
import mm.com.mytel.training_project.library_management_system.dto.request.CategoryRequest;
import org.springframework.http.ResponseEntity;

public interface CategoryService {

    ResponseEntity<Basic> addCategory(CategoryRequest categoryRequest);

    ResponseEntity<Basic> updateCategory(Long id, CategoryRequest categoryRequest);

    ResponseEntity<Basic> deleteCategory(Long id);

    ResponseEntity<Basic> listAllCategories(int page, int size);
}
