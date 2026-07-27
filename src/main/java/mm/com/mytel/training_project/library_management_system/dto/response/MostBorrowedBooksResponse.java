package mm.com.mytel.training_project.library_management_system.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MostBorrowedBooksResponse {

    private Long bookId;
    private String isbn;
    private String title;
    private String publisher;
    private Long totalBorrowCount;
}
