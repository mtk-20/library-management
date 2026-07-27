package mm.com.mytel.training_project.library_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mm.com.mytel.training_project.library_management_system.enums.BorrowStatus;
import mm.com.mytel.training_project.library_management_system.enums.DueStatus;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DueDateResponse {

    private String title;
    private String isbn;
    private LocalDate dueDate;
    private Long remainingDays;
    private Long overdueDays;
    private BorrowStatus status;
    private DueStatus dueStatus;
}
