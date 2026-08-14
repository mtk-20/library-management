package mm.com.mytel.training_project.library_management_system.dto.response;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
public class BorrowHistoryResponse {

    private String title;
    private String isbn;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Long memberId;
}
