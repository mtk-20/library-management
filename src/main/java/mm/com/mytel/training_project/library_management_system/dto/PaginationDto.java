package mm.com.mytel.training_project.library_management_system.dto;

import lombok.Data;

@Data
public class PaginationDto {
    private int page = 0;
    private int size = 10;
}
