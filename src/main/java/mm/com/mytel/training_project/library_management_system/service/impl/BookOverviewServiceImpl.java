package mm.com.mytel.training_project.library_management_system.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mm.com.mytel.training_project.library_management_system.common.constant.ErrorCode;
import mm.com.mytel.training_project.library_management_system.common.response.ResponseFactory;
import mm.com.mytel.training_project.library_management_system.dto.response.MostBorrowedBooksResponse;
import mm.com.mytel.training_project.library_management_system.entity.Book;
import mm.com.mytel.training_project.library_management_system.entity.BorrowRecord;
import mm.com.mytel.training_project.library_management_system.enums.BookStatus;
import mm.com.mytel.training_project.library_management_system.enums.BorrowStatus;
import mm.com.mytel.training_project.library_management_system.repo.BookRepo;
import mm.com.mytel.training_project.library_management_system.repo.BorrowRecordRepo;
import mm.com.mytel.training_project.library_management_system.service.BookOverviewService;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookOverviewServiceImpl implements BookOverviewService {

    private final BookRepo bookRepo;
    private final BorrowRecordRepo borrowRecordRepo;
    private final ResponseFactory responseFactory;

    @Override
    public ResponseEntity<?> currentlyBorrowedBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BorrowRecord> record = borrowRecordRepo.findByStatus(BorrowStatus.BORROWED, pageable);

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                record,
                ErrorCode.OK,
                "Currently borrowed books list"
        );
    }

    @Override
    public ResponseEntity<?> totalAvailableBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> book = bookRepo.findByBookStatusAndAvailableCopiesGreaterThan(BookStatus.AVAILABLE, 0, pageable);

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                book,
                ErrorCode.OK,
                "Total available books"
        );
    }

    @Override
    public ResponseEntity<?> mostBorrowedBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MostBorrowedBooksResponse> response = borrowRecordRepo.findMostBorrowedBooks(pageable);

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                response,
                ErrorCode.OK,
                "Most borrowed books"
        );
    }

}
