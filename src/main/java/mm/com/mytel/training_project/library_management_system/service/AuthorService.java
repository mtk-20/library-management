package mm.com.mytel.training_project.library_management_system.service;

import mm.com.mytel.training_project.library_management_system.common.response.Basic;
import mm.com.mytel.training_project.library_management_system.dto.request.AuthorRequest;
import mm.com.mytel.training_project.library_management_system.dto.request.AuthorUpdateRequest;
import mm.com.mytel.training_project.library_management_system.dto.request.CategoryRequest;
import org.springframework.http.ResponseEntity;

public interface AuthorService {

    ResponseEntity<Basic> addAuthor(AuthorRequest authorRequest);

    ResponseEntity<Basic> updateAuthor(Long id, AuthorUpdateRequest authorUpdateRequest);

    ResponseEntity<Basic> deleteAuthor(Long id);

    ResponseEntity<Basic> listAllAuthors(int page, int size);

    ResponseEntity<Basic> getAuthorById(Long id);
}
