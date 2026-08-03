package mm.com.mytel.training_project.library_management_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mm.com.mytel.training_project.library_management_system.dto.request.AuthorRequest;
import mm.com.mytel.training_project.library_management_system.dto.request.AuthorUpdateRequest;
import mm.com.mytel.training_project.library_management_system.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/author")
public class AuthorController {

    private final AuthorService authorService;

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @PostMapping()
    public ResponseEntity<?> handleAddAuthor(@Valid @RequestBody AuthorRequest authorRequest) {
        return authorService.addAuthor(authorRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @PatchMapping()
    public ResponseEntity<?> handleUpdateCAuthor(@RequestParam Long id, @Valid @RequestBody AuthorUpdateRequest authorUpdateRequest) {
        return authorService.updateAuthor(id, authorUpdateRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @DeleteMapping()
    public ResponseEntity<?> handleDeleteAuthor(@RequestParam Long id) {
        return authorService.deleteAuthor(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'MEMBER')")
    @GetMapping()
    public ResponseEntity<?> handleListAllAuthors(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return authorService.listAllAuthors(page, size);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'MEMBER')")
    @GetMapping("/id")
    public ResponseEntity<?> handleGetAuthorById(@RequestParam Long id) {
        return authorService.getAuthorById(id);
    }
}
