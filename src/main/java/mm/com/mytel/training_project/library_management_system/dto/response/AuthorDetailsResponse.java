package mm.com.mytel.training_project.library_management_system.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class AuthorDetailsResponse {

    private String authorName;
    private String biography;
    private String nationality;
    private List<BooksByAuthorResponse> booksByAuthor;
}
