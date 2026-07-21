package mm.com.mytel.training_project.library_management_system.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BookUpdateRequest {

    private String description;

    @Min(1)
    private Integer totalCopies;
}
