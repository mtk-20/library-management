package mm.com.mytel.training_project.library_management_system.dto.request;

import lombok.Data;
import mm.com.mytel.training_project.library_management_system.dto.PaginationDto;

@Data
public class BookSearchRequest extends PaginationDto {

    private String isbn;
    private String title;
    private Long authorId;
    private Long categoryId;
}
