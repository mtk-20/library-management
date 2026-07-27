package mm.com.mytel.training_project.library_management_system.service;

import org.springframework.http.ResponseEntity;

public interface BookOverviewService {

    ResponseEntity<?> currentlyBorrowedBooks(int page, int size);

    ResponseEntity<?> totalAvailableBooks(int page, int size);

    ResponseEntity<?> mostBorrowedBooks(int page, int size);
}
