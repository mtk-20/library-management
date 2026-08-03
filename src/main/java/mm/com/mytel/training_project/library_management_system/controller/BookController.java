package mm.com.mytel.training_project.library_management_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mm.com.mytel.training_project.library_management_system.dto.request.*;
import mm.com.mytel.training_project.library_management_system.enums.BorrowStatus;
import mm.com.mytel.training_project.library_management_system.service.BookBorrowAndReturnService;
import mm.com.mytel.training_project.library_management_system.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookController {

    private final BookService bookService;
    private final BookBorrowAndReturnService bookBorrowAndReturnService;

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @PostMapping()
    public ResponseEntity<?> handleAddBook(@Valid @RequestBody BookRequest bookRequest) {
        return bookService.addBook(bookRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @PatchMapping()
    public ResponseEntity<?> handleUpdateBook(@RequestParam Long id, @Valid @RequestBody BookUpdateRequest bookUpdateRequest) {
        return bookService.updateBook(id, bookUpdateRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @DeleteMapping()
    public ResponseEntity<?> handleDeleteBook(@RequestParam Long id) {
        return bookService.deleteBook(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'MEMBER')")
    @GetMapping()
    public ResponseEntity<?> handleListAllBooks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return bookService.listAllBooks(page, size);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'MEMBER')")
    @GetMapping("/filter")
    public ResponseEntity<?> handleSearchBook(@RequestBody BookSearchRequest bookSearchRequest) {
        return bookService.searchBook(bookSearchRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'MEMBER')")
    @GetMapping("/id")
    public ResponseEntity<?> handleGetBookById(@RequestParam Long id) {
        return bookService.getBookById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @PostMapping("/borrow")
    public ResponseEntity<?> handleBorrowBook(@RequestBody BookBorrowRequest bookBorrowRequest) {
        return bookBorrowAndReturnService.borrowBook(bookBorrowRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    @PostMapping("/return")
    public ResponseEntity<?> handleReturnBook(@RequestBody BookReturnRequest bookReturnRequest) {
        return bookBorrowAndReturnService.returnBook(bookReturnRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'MEMBER')")
    @GetMapping("/track-borrow-history")
    public ResponseEntity<?> handleTrackBorrowHistory(@RequestParam BorrowStatus status, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return bookBorrowAndReturnService.trackBorrowRecord(status, page, size);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'MEMBER')")
    @GetMapping("/track-due-date")
    public ResponseEntity<?> handleTrackDueDate(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return bookBorrowAndReturnService.trackDueDate(page, size);
    }
}
