package mm.com.mytel.training_project.library_management_system.dto.request;

import lombok.Data;
import mm.com.mytel.training_project.library_management_system.dto.Pagination;

@Data
public class BookSearchRequest extends Pagination {

    private String isbn;
    private String title;
    private Long authorId;
    private Long categoryId;
}
