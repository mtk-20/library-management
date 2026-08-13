package mm.com.mytel.training_project.library_management_system.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookBorrowRequest {

    @NotNull
    private Long bookId;

    @NotNull
    private Long memberId;
}
