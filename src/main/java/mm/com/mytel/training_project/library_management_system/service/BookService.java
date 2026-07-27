package mm.com.mytel.training_project.library_management_system.service;

import mm.com.mytel.training_project.library_management_system.common.response.Basic;
import mm.com.mytel.training_project.library_management_system.dto.request.BookRequest;
import mm.com.mytel.training_project.library_management_system.dto.request.BookSearchRequest;
import mm.com.mytel.training_project.library_management_system.dto.request.BookUpdateRequest;
import org.springframework.http.ResponseEntity;

public interface BookService {

    ResponseEntity<Basic> addBook(BookRequest bookRequest);

    ResponseEntity<Basic> updateBook(Long id, BookUpdateRequest bookUpdateRequest);

    ResponseEntity<Basic> deleteBook(Long id);

    ResponseEntity<Basic> listAllBooks(int page, int size);

    ResponseEntity<Basic> searchBook(BookSearchRequest bookSearchRequest);

    ResponseEntity<?> getBookById(Long id);
}
