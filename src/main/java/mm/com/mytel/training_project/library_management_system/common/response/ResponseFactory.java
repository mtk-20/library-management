package mm.com.mytel.training_project.library_management_system.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseFactory {

    public ResponseEntity<Basic> buildSuccess(HttpStatus status, Object result, String errorCode, String message) {
        Basic basic = new Basic();
        basic.setSuccess(true);
        basic.setMessage(message);
        basic.setResult(result);
        basic.setCode(errorCode);
        return ResponseEntity.status(status).body(basic);
    }

    public ResponseEntity<Basic> buildError(HttpStatus status, Object result, String errorCode, String message) {
        Basic basic = new Basic();
        basic.setSuccess(false);
        basic.setMessage(message);
        basic.setResult(result);
        basic.setCode(errorCode);
        return ResponseEntity.status(status).body(basic);
    }
}
