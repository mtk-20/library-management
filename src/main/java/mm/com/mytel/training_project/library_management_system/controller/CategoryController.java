package mm.com.mytel.training_project.library_management_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mm.com.mytel.training_project.library_management_system.dto.request.BookUpdateRequest;
import mm.com.mytel.training_project.library_management_system.dto.request.CategoryRequest;
import mm.com.mytel.training_project.library_management_system.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping()
    public ResponseEntity<?> handleAddCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        return categoryService.addCategory(categoryRequest);
    }

    @PatchMapping()
    public ResponseEntity<?> handleUpdateCategory(@RequestParam Long id, @Valid @RequestBody CategoryRequest categoryRequest) {
        return categoryService.updateCategory(id, categoryRequest);
    }

    @DeleteMapping()
    public ResponseEntity<?> handleDeleteBCategory(@RequestParam Long id) {
        return categoryService.deleteCategory(id);
    }

    @GetMapping()
    public ResponseEntity<?> handleListAllCategories(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return categoryService.listAllCategories(page, size);
    }
}
