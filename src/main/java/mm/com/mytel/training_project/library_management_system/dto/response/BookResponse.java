package mm.com.mytel.training_project.library_management_system.dto.response;

import lombok.Data;
import mm.com.mytel.training_project.library_management_system.enums.BookStatus;

@Data
public class BookResponse {

    private Long id;
    private String isbn;
    private String title;
    private Long authorId;
    private String authorName;
    private Long categoryId;
    private String categoryName;
    private String publisher;
    private Integer publicationYear;
    private String language;
    private String description;
    private Integer totalCopies;
    private Integer availableCopies;
    private BookStatus bookStatus;
}
