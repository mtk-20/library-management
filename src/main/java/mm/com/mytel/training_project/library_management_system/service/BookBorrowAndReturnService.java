package mm.com.mytel.training_project.library_management_system.service;

import mm.com.mytel.training_project.library_management_system.dto.request.BookBorrowRequest;
import org.springframework.http.ResponseEntity;
import mm.com.mytel.training_project.library_management_system.dto.request.BookReturnRequest;
import mm.com.mytel.training_project.library_management_system.enums.BorrowStatus;

public interface BookBorrowAndReturnService {

    ResponseEntity<?> borrowBook(BookBorrowRequest bookBorrowRequest);

    ResponseEntity<?> returnBook(BookReturnRequest bookReturnRequest);

    ResponseEntity<?> trackBorrowRecord(BorrowStatus status,int page, int size);

    ResponseEntity<?> trackDueDate(int page, int size);
}
