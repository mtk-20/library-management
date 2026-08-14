package mm.com.mytel.training_project.library_management_system.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BookUpdateRequest {

    private String isbn;
    private String title;
    private Long authorId;
    private Long categoryId;
    private String publisher;
    private Integer publicationYear;
    private String language;
    private String description;
    @Min(1)
    private Integer totalCopies;
}
