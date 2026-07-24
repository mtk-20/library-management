package mm.com.mytel.training_project.library_management_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mm.com.mytel.training_project.library_management_system.dto.request.BookRequest;
import mm.com.mytel.training_project.library_management_system.dto.request.BookSearchRequest;
import mm.com.mytel.training_project.library_management_system.dto.request.BookUpdateRequest;
import mm.com.mytel.training_project.library_management_system.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookController {

    private final BookService bookService;

    @PostMapping()
    public ResponseEntity<?> handleAddBook(@Valid @RequestBody BookRequest bookRequest) {
        return bookService.addBook(bookRequest);
    }

    @PatchMapping()
    public ResponseEntity<?> handleUpdateBook(@RequestParam Long id, @Valid @RequestBody BookUpdateRequest bookUpdateRequest) {
        return bookService.updateBook(id, bookUpdateRequest);
    }

    @DeleteMapping()
    public ResponseEntity<?> handleDeleteBook(@RequestParam Long id) {
        return bookService.deleteBook(id);
    }

    @GetMapping()
    public ResponseEntity<?> handleListAllBooks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return bookService.listAllBooks(page, size);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> handleSearchBook(@RequestBody BookSearchRequest bookSearchRequest) {
        return bookService.searchBook(bookSearchRequest);
    }
}
