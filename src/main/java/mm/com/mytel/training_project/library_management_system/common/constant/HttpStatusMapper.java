package mm.com.mytel.training_project.library_management_system.common.constant;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class HttpStatusMapper {

    private static final Map<String, HttpStatus> ERROR_CODE_TO_STATUS = new HashMap<>();

    static {
        ERROR_CODE_TO_STATUS.put(ErrorCode.OK, HttpStatus.OK);
        ERROR_CODE_TO_STATUS.put(ErrorCode.CREATED, HttpStatus.CREATED);
        ERROR_CODE_TO_STATUS.put(ErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST);
        ERROR_CODE_TO_STATUS.put(ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        ERROR_CODE_TO_STATUS.put(ErrorCode.FORBIDDEN, HttpStatus.FORBIDDEN);
        ERROR_CODE_TO_STATUS.put(ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND);
        ERROR_CODE_TO_STATUS.put(ErrorCode.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static HttpStatus getStatus(String errorCode) {
        return ERROR_CODE_TO_STATUS.getOrDefault(errorCode, HttpStatus.BAD_REQUEST);
    }
}
