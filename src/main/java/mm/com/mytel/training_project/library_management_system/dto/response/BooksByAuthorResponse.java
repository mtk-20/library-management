package mm.com.mytel.training_project.library_management_system.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BooksByAuthorResponse {

    private String isbn;
    private String title;
    private String publisher;
    private Integer publicationYear;
    private String language;
    private String description;
}
