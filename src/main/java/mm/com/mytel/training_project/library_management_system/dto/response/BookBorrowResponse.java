package mm.com.mytel.training_project.library_management_system.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mm.com.mytel.training_project.library_management_system.enums.BorrowStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookBorrowResponse {
    
    private Long borrowId;
    private String memberName;
    private String bookTitle;
    private String isbn;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private BorrowStatus status;
    private Integer remainingCopies;
}
