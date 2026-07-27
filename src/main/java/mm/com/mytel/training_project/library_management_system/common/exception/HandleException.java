package mm.com.mytel.training_project.library_management_system.common.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mm.com.mytel.training_project.library_management_system.common.constant.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class HandleException extends ResponseEntityExceptionHandler {

    private final ResponseFactoryForException responseFactory;
    private final ResponseFactoryForException responseFactoryForException;

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<?> handleCommonException(CommonException e) {
        log.error("Error: ", e);
        if (ErrorCode.BAD_REQUEST.equals(e.getErrorCode())) {
            return responseFactory.badRequest(e.getErrorCode(), e.getMessage());
        }
        if (ErrorCode.UNAUTHORIZED.equals(e.getErrorCode())) {
            return responseFactory.unauthorized(e.getErrorCode(), e.getMessage());
        }
        if (ErrorCode.FORBIDDEN.equals(e.getErrorCode())) {
            return responseFactory.forbidden(e.getErrorCode(), e.getMessage());
        }
        if (ErrorCode.NOT_FOUND.equals(e.getErrorCode())) {
            return responseFactory.notFound(e.getErrorCode(), e.getMessage());
        }
        return responseFactory.internalError(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException: ", e);
        return responseFactory.internalError(ErrorCode.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleExceptions(Exception e) {
        log.error("Unexpected Exception: ", e);
        return responseFactory.internalError(ErrorCode.INTERNAL_SERVER_ERROR, "An internal server error occurred. Please contact support.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException e) {
        log.error("Access Denied Exception: ", e);
        return responseFactoryForException.forbidden(ErrorCode.FORBIDDEN, "Access denied.");
    }
}