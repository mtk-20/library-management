package mm.com.mytel.training_project.library_management_system.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.*;

import mm.com.mytel.training_project.library_management_system.enums.BorrowStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookReturnResponse {

    private String memberName;
    private String bookTitle;
    private String isbn;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BigDecimal fineAmount;
    private BorrowStatus status;
    private Integer availableCopies;
}
