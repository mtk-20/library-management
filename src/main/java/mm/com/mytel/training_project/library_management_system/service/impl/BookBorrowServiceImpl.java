package mm.com.mytel.training_project.library_management_system.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mm.com.mytel.training_project.library_management_system.common.constant.ErrorCode;
import mm.com.mytel.training_project.library_management_system.common.exception.CommonException;
import mm.com.mytel.training_project.library_management_system.common.response.ResponseFactory;
import mm.com.mytel.training_project.library_management_system.common.util.UserUtil;
import mm.com.mytel.training_project.library_management_system.dto.request.BookBorrowRequest;
import mm.com.mytel.training_project.library_management_system.entity.Book;
import mm.com.mytel.training_project.library_management_system.entity.BorrowRecord;
import mm.com.mytel.training_project.library_management_system.entity.Member;
import mm.com.mytel.training_project.library_management_system.entity.User;
import mm.com.mytel.training_project.library_management_system.enums.BorrowStatus;
import mm.com.mytel.training_project.library_management_system.repo.BookRepo;
import mm.com.mytel.training_project.library_management_system.repo.BorrowRecordRepo;
import mm.com.mytel.training_project.library_management_system.repo.MemberRepo;
import mm.com.mytel.training_project.library_management_system.repo.UserRepo;
import mm.com.mytel.training_project.library_management_system.service.BookBorrowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookBorrowServiceImpl implements BookBorrowService {

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

        Member member = getCurrentMember();
        Long memberId = member.getId();

        Book book = bookRepo.findById(bookBorrowRequest.getBookId()).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Book not found."));

        if (book.getAvailableCopies() <= 0) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "Book is not available.");
        }

        BorrowRecord record = new BorrowRecord();
        record.setMemberId(memberId);
        record.setBookId(book.getId());
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusMonths(1));
        record.setFineAmount(BigDecimal.ZERO);
        record.setStatus(BorrowStatus.BORROWED);
        borrowRecordRepo.save(record);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepo.save(book);

        return responseFactory.buildSuccess(
                HttpStatus.CREATED,
                record,
                ErrorCode.CREATED,
                "Book borrow success."
        );
    }
}
