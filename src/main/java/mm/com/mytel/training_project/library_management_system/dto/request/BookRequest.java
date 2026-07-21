package mm.com.mytel.training_project.library_management_system.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookRequest {

    @NotBlank
    private String isbn;

    @NotBlank
    private String title;

    @NotNull
    private Long authorId;

    @NotNull
    private Long categoryId;

    @NotNull
    private Integer publicationYear;

    @NotBlank
    private String language;

    @NotBlank
    private String description;

    @NotNull
    @Min(1)
    private Integer totalCopies;
}
