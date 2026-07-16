package mm.com.mytel.training_project.library_management_system.common.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Basic {

    private boolean success;
    private String code;
    private Object result;
    private String message;
}
