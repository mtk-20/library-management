package mm.com.mytel.training_project.library_management_system.common.exception;

import mm.com.mytel.training_project.library_management_system.common.response.Basic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseFactoryForException {

    public ResponseEntity<Basic> badRequest(String errorCode, String message) {
        return createResponse(HttpStatus.BAD_REQUEST, errorCode, message);
    }

    public ResponseEntity<Basic> unauthorized(String errorCode, String message) {
        return createResponse(HttpStatus.UNAUTHORIZED, errorCode, message);
    }

    public ResponseEntity<Basic> forbidden(String errorCode, String message) {
        return createResponse(HttpStatus.FORBIDDEN, errorCode, message);
    }

    public ResponseEntity<Basic> notFound(String errorCode, String message) {
        return createResponse(HttpStatus.NOT_FOUND, errorCode, message);
    }

    public ResponseEntity<Basic> internalError(String errorCode, String message) {
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorCode, message);
    }

    private ResponseEntity<Basic> createResponse(HttpStatus status, String errorCode, String message) {
        Basic basic = new Basic();
        basic.setSuccess(false);
        basic.setResult(null);
        basic.setMessage(message);
        basic.setCode(errorCode);
        return ResponseEntity.status(status).body(basic);
    }
}
