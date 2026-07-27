package mm.com.mytel.training_project.library_management_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mm.com.mytel.training_project.library_management_system.service.BookOverviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/overview")
public class BookOverViewController {

private final BookOverviewService overviewService;
    
    @GetMapping("/currently-borrowed")
    public ResponseEntity<?> getCurrentlyBorrowedBooks(
           @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return overviewService.currentlyBorrowedBooks(page, size);
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableBooks(
           @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return overviewService.totalAvailableBooks(page, size);
    }

    @GetMapping("/most-borrowed")
    public ResponseEntity<?> getMostBorrowedBooks(
           @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return overviewService.mostBorrowedBooks(page, size);
    }
}
