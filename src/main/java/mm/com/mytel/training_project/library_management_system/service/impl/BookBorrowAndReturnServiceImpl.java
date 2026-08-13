package mm.com.mytel.training_project.library_management_system.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mm.com.mytel.training_project.library_management_system.common.constant.ErrorCode;
import mm.com.mytel.training_project.library_management_system.common.exception.CommonException;
import mm.com.mytel.training_project.library_management_system.common.response.ResponseFactory;
import mm.com.mytel.training_project.library_management_system.common.util.UserUtil;
import mm.com.mytel.training_project.library_management_system.dto.request.BookBorrowRequest;
import mm.com.mytel.training_project.library_management_system.dto.request.BookReturnRequest;
import mm.com.mytel.training_project.library_management_system.dto.response.BookBorrowResponse;
import mm.com.mytel.training_project.library_management_system.dto.response.BookReturnResponse;
import mm.com.mytel.training_project.library_management_system.dto.response.BorrowHistoryResponse;
import mm.com.mytel.training_project.library_management_system.dto.response.DueDateResponse;
import mm.com.mytel.training_project.library_management_system.entity.Book;
import mm.com.mytel.training_project.library_management_system.entity.BorrowRecord;
import mm.com.mytel.training_project.library_management_system.entity.Member;
import mm.com.mytel.training_project.library_management_system.entity.User;
import mm.com.mytel.training_project.library_management_system.enums.BookStatus;
import mm.com.mytel.training_project.library_management_system.enums.BorrowStatus;
import mm.com.mytel.training_project.library_management_system.enums.DueStatus;
import mm.com.mytel.training_project.library_management_system.repo.BookRepo;
import mm.com.mytel.training_project.library_management_system.repo.BorrowRecordRepo;
import mm.com.mytel.training_project.library_management_system.repo.MemberRepo;
import mm.com.mytel.training_project.library_management_system.repo.UserRepo;
import mm.com.mytel.training_project.library_management_system.service.BookBorrowAndReturnService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookBorrowAndReturnServiceImpl implements BookBorrowAndReturnService {

    private final UserUtil userUtil;
    private final UserRepo userRepo;
    private final BookRepo bookRepo;
    private final MemberRepo memberRepo;
    private final BorrowRecordRepo borrowRecordRepo;
    private final ResponseFactory responseFactory;

    //    get logged in member
    public Member getCurrentMember() {
        String username = userUtil.getCurrentLoginUser();
        User user = userRepo.findByUserName(username).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "User not found."));

        return memberRepo.findByUserId(user.getId()).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Member not found."));
    }

    @Override
    public ResponseEntity<?> borrowBook(BookBorrowRequest bookBorrowRequest) {
        Member member = memberRepo.findById(bookBorrowRequest.getMemberId()).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Member not found."));
        Long memberId = member.getId();

        Book book = bookRepo.findById(bookBorrowRequest.getBookId()).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Book not found."));

        if (book.getAvailableCopies() <= 0) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "Book is not available.");
        }
        if (borrowRecordRepo.existsByMemberIdAndBookIdAndStatus(memberId, book.getId(), BorrowStatus.BORROWED)) {
            throw new CommonException(ErrorCode.BAD_REQUEST,"You have already borrowed this book.");
        }

        BorrowRecord record = new BorrowRecord();
        record.setMemberId(memberId);
        record.setBookId(book.getId());
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusMonths(1));
        record.setFineAmount(BigDecimal.ZERO);
        record.setStatus(BorrowStatus.BORROWED);
        BorrowRecord savedRecord = borrowRecordRepo.save(record);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepo.save(book);

        BookBorrowResponse response = BookBorrowResponse.builder()
                .borrowId(savedRecord.getId())
                .memberName(member.getMemberName())
                .bookTitle(book.getTitle())
                .isbn(book.getIsbn())
                .borrowDate(savedRecord.getBorrowDate())
                .dueDate(savedRecord.getDueDate())
                .status(savedRecord.getStatus())
                .remainingCopies(book.getAvailableCopies())
                .build();

        return responseFactory.buildSuccess(
                HttpStatus.CREATED,
                response,
                ErrorCode.CREATED,
                "Book borrow success."
        );
    }

    @Transactional
    @Override
    public ResponseEntity<?> returnBook(BookReturnRequest bookReturnRequest) {
        Member member = memberRepo.findById(bookReturnRequest.getMemberId()).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Member not found."));
        Long memberId = member.getId();
        BorrowRecord record = borrowRecordRepo.findByIdAndMemberId(bookReturnRequest.getBorrowId(), memberId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Borrow record not found."));

        if (record.getStatus() == BorrowStatus.RETURNED) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "Book has already been returned.");
        }

        Book book = bookRepo.findById(record.getBookId()).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Book not found."));

        LocalDate today = LocalDate.now();

        BigDecimal fineAmount = calculateFine(record.getDueDate(), today);

        record.setReturnDate(today);
        record.setFineAmount(fineAmount);
        record.setStatus(BorrowStatus.RETURNED);
        borrowRecordRepo.save(record);

        book.setAvailableCopies(book.getAvailableCopies() + 1);

        if (book.getAvailableCopies() > 0) {
            book.setBookStatus(BookStatus.AVAILABLE);
        }

        bookRepo.save(book);

        BookReturnResponse response = BookReturnResponse.builder()
                .memberName(member.getMemberName())
                .bookTitle(book.getTitle())
                .isbn(book.getIsbn())
                .borrowDate(record.getBorrowDate())
                .dueDate(record.getDueDate())
                .returnDate(record.getReturnDate())
                .fineAmount(record.getFineAmount())
                .status(record.getStatus())
                .availableCopies(book.getAvailableCopies())
                .build();

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                response,
                ErrorCode.OK,
                "Book returned successfully."

        );
    }

    private BigDecimal calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (!returnDate.isAfter(dueDate)) {
            return BigDecimal.ZERO;
        }
        long lateDays = ChronoUnit.DAYS.between(dueDate, returnDate);

        return BigDecimal.valueOf(lateDays * 500);
    }

    @Override
    public ResponseEntity<?> trackBorrowRecord(BorrowStatus status, int page, int size) {
        Member member = getCurrentMember();

        Pageable pageable = PageRequest.of(page, size);
        Page<BorrowRecord> recordPage = borrowRecordRepo.findByMemberIdAndStatus(member.getId(), status, pageable);

        Page<BorrowHistoryResponse> response = recordPage.map(record -> {
            Book book = bookRepo.findById(record.getBookId()).orElseThrow(() ->
                    new CommonException(ErrorCode.NOT_FOUND, "Book not found."));

            return BorrowHistoryResponse.builder()
                    .title(book.getTitle())
                    .isbn(book.getIsbn())
                    .borrowDate(record.getBorrowDate())
                    .dueDate(record.getDueDate())
                    .returnDate(record.getReturnDate())
                    .build();
        });

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                response,
                ErrorCode.OK,
                "Track borrow history success."
        );
    }

    @Override
    public ResponseEntity<?> trackDueDate(int page, int size) {
        Member member = getCurrentMember();

        Pageable pageable = PageRequest.of(page, size);
        Page<BorrowRecord> records = borrowRecordRepo.findByDueDate(member.getId(), pageable);

        Page<DueDateResponse> response = records.map(record -> {
            Book book = bookRepo.findById(record.getBookId()).orElseThrow(() ->
                    new CommonException(ErrorCode.NOT_FOUND, "Book not found."));

            long days = ChronoUnit.DAYS.between(LocalDate.now(), record.getDueDate());

            Long remainingDays = null;
            Long overdueDays = null;

            DueStatus dueStatus;
            if (days > 0) {
                remainingDays = days;
                dueStatus = DueStatus.DUE_SOON;
            } else if (days == 0) {
                remainingDays = 0L;
                dueStatus = DueStatus.DUE_TODAY;
            } else {
                overdueDays = Math.abs(days);
                dueStatus = DueStatus.OVERDUE;
            }

            return DueDateResponse.builder()
                    .title(book.getTitle())
                    .isbn(book.getIsbn())
                    .dueDate(record.getDueDate())
                    .remainingDays(remainingDays)
                    .overdueDays(overdueDays)
                    .status(record.getStatus())
                    .dueStatus(dueStatus)
                    .build();
        });

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                response,
                ErrorCode.OK,
                "Track due date success."
        );
    }
}
