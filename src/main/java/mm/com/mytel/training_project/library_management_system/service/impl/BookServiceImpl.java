package mm.com.mytel.training_project.library_management_system.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mm.com.mytel.training_project.library_management_system.common.constant.ErrorCode;
import mm.com.mytel.training_project.library_management_system.common.exception.CommonException;
import mm.com.mytel.training_project.library_management_system.common.response.Basic;
import mm.com.mytel.training_project.library_management_system.common.response.ResponseFactory;
import mm.com.mytel.training_project.library_management_system.dto.request.BookRequest;
import mm.com.mytel.training_project.library_management_system.dto.request.BookSearchRequest;
import mm.com.mytel.training_project.library_management_system.dto.request.BookUpdateRequest;
import mm.com.mytel.training_project.library_management_system.dto.response.BookResponse;
import mm.com.mytel.training_project.library_management_system.entity.Author;
import mm.com.mytel.training_project.library_management_system.entity.Book;
import mm.com.mytel.training_project.library_management_system.entity.Category;
import mm.com.mytel.training_project.library_management_system.enums.BookStatus;
import mm.com.mytel.training_project.library_management_system.repo.AuthorRepo;
import mm.com.mytel.training_project.library_management_system.repo.BookRepo;
import mm.com.mytel.training_project.library_management_system.repo.CategoryRepo;
import mm.com.mytel.training_project.library_management_system.service.BookService;
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
public class BookServiceImpl implements BookService {

    private final BookRepo bookRepo;
    private final AuthorRepo authorRepo;
    private final CategoryRepo categoryRepo;
    private final ResponseFactory responseFactory;

    //    add book
    @Override
    public ResponseEntity<Basic> addBook(BookRequest bookRequest) {
        Author author = authorRepo.findById(bookRequest.getAuthorId()).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Author not found"));
        Category category = categoryRepo.findById(bookRequest.getCategoryId()).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Category not found"));

        Book entity = new Book();
        entity.setIsbn(bookRequest.getIsbn());
        entity.setTitle(bookRequest.getTitle());
        entity.setAuthorId(author.getId());
        entity.setCategoryId(category.getId());
        entity.setPublisher(author.getAuthorName());
        entity.setPublicationYear(bookRequest.getPublicationYear());
        entity.setLanguage(bookRequest.getLanguage());
        entity.setDescription(bookRequest.getDescription());
        entity.setTotalCopies(bookRequest.getTotalCopies());
        entity.setAvailableCopies(bookRequest.getTotalCopies());
        entity.setBookStatus(BookStatus.AVAILABLE);
        Book savedBook = bookRepo.save(entity);

        BookResponse response = toBookResponse(savedBook, author, category);

        return responseFactory.buildSuccess(
                HttpStatus.CREATED,
                response,
                ErrorCode.CREATED,
                "Book create success."
        );
    }

    private BookResponse toBookResponse(Book savedBook, Author author, Category category) {
        BookResponse response = new BookResponse();
        response.setId(savedBook.getId());
        response.setIsbn(savedBook.getIsbn());
        response.setTitle(savedBook.getTitle());
        response.setAuthorId(author.getId());
        response.setAuthorName(author.getAuthorName());
        response.setCategoryId(category.getId());
        response.setCategoryName(category.getCategoryName());
        response.setPublisher(savedBook.getPublisher());
        response.setPublicationYear(savedBook.getPublicationYear());
        response.setLanguage(savedBook.getLanguage());
        response.setDescription(savedBook.getDescription());
        response.setTotalCopies(savedBook.getTotalCopies());
        response.setAvailableCopies(savedBook.getAvailableCopies());
        response.setBookStatus(savedBook.getBookStatus());

        return response;
    }

    //    update book (only description and total copies)
    @Override
    public ResponseEntity<Basic> updateBook(Long id, BookUpdateRequest bookUpdateRequest) {
        Book entity = bookRepo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Book not found."));
        entity.setDescription(bookUpdateRequest.getDescription());
        entity.setTotalCopies(bookUpdateRequest.getTotalCopies());
        bookRepo.save(entity);

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                entity,
                ErrorCode.OK,
                "Book update success."
        );
    }

    //    delete book
    @Override
    public ResponseEntity<Basic> deleteBook(Long id) {
        Book entity = bookRepo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Book not found."));
        bookRepo.delete(entity);

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                null,
                ErrorCode.OK,
                "Book delete success."
        );
    }

    //    list all books
    @Override
    public ResponseEntity<Basic> listAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> entities = bookRepo.findAllByOrderByPublicationYearDescIdAsc(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("books", entities);

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                response,
                ErrorCode.OK,
                "Book list request success."
        );
    }

    //    filter books
    @Override
    public ResponseEntity<Basic> searchBook(BookSearchRequest bookSearchRequest) {
        Pageable pageable = PageRequest.of(bookSearchRequest.getPage(), bookSearchRequest.getSize());
        Page<Book> bookPage = bookRepo.filterBooks(
                bookSearchRequest.getIsbn(), bookSearchRequest.getTitle(), bookSearchRequest.getAuthorId(), bookSearchRequest.getCategoryId(), pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("books", bookPage);

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                response,
                ErrorCode.OK,
                "Book search query success."
        );
    }
}
