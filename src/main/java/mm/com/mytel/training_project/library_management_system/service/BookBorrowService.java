package mm.com.mytel.training_project.library_management_system.service;

import mm.com.mytel.training_project.library_management_system.dto.request.BookBorrowRequest;
import org.springframework.http.ResponseEntity;

public interface BookBorrowService {

    ResponseEntity<?> borrowBook(BookBorrowRequest bookBorrowRequest);
}
