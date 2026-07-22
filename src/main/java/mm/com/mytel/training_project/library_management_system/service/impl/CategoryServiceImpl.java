package mm.com.mytel.training_project.library_management_system.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mm.com.mytel.training_project.library_management_system.common.constant.ErrorCode;
import mm.com.mytel.training_project.library_management_system.common.exception.CommonException;
import mm.com.mytel.training_project.library_management_system.common.response.Basic;
import mm.com.mytel.training_project.library_management_system.common.response.ResponseFactory;
import mm.com.mytel.training_project.library_management_system.dto.request.CategoryRequest;
import mm.com.mytel.training_project.library_management_system.dto.response.CategoryResponse;
import mm.com.mytel.training_project.library_management_system.entity.Book;
import mm.com.mytel.training_project.library_management_system.entity.Category;
import mm.com.mytel.training_project.library_management_system.repo.CategoryRepo;
import mm.com.mytel.training_project.library_management_system.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    private final ResponseFactory responseFactory;

    @Override
    public ResponseEntity<Basic> addCategory(CategoryRequest categoryRequest) {
        Category entity = new Category();
        if (categoryRepo.existsByCategoryName(categoryRequest.getCategoryName())) {
            throw new CommonException(ErrorCode.DUPLICATE, "Category name already exists.");
        }
        entity.setCategoryName(categoryRequest.getCategoryName());
        Category savedCategory = categoryRepo.save(entity);

        CategoryResponse response = new CategoryResponse();
        response.setCategoryName(savedCategory.getCategoryName());

        return responseFactory.buildSuccess(
                HttpStatus.CREATED,
                response,
                ErrorCode.CREATED,
                "Category create success."
        );
    }

    @Override
    public ResponseEntity<Basic> updateCategory(Long id, CategoryRequest categoryRequest) {
        Category entity = categoryRepo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Category not found."));
        entity.setCategoryName(categoryRequest.getCategoryName());
        categoryRepo.save(entity);

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                entity,
                ErrorCode.OK,
                "Category update success."
        );
    }

    @Override
    public ResponseEntity<Basic> deleteCategory(Long id) {
        Category entity = categoryRepo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Category not found."));
        categoryRepo.delete(entity);

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                null,
                ErrorCode.OK,
                "Category delete success."
        );
    }

    @Override
    public ResponseEntity<Basic> listAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> entities = categoryRepo.findAllCategories(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("categories", entities);

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                response,
                ErrorCode.OK,
                "Category list request success."
        );
    }
}
