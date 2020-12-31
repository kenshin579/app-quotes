package kr.pe.advenoh.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, ErrorCode.REQUEST_INVALID.getMessage(),
                ErrorCode.REQUEST_INVALID.getCode(), details);
        return new ResponseEntity<Object>(error, error.getStatus());
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseEntity<?> handleBindException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, ErrorCode.REQUEST_INVALID.getMessage(),
                ErrorCode.REQUEST_INVALID.getCode(), details);
        return new ResponseEntity<Object>(error, error.getStatus());
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseEntity<?> handleException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error("[exception] uri : {} {} msg : {} {}", request.getRequestURI(), request.getMethod(), ex.getMessage(), ex.getClass(), ex);
        //todo : 조금 더 좋드 더 담을 수 있도록 수정하기
        final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());

        log.error("handleException error :: uri -> {} msg -> {} class -> {} e -> {}",
                new Object[]{request.getRequestURI(), ex.getMessage(), ex.getClass(), ex});

        return new ResponseEntity<Object>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler({ApiException.class})
    @ResponseBody
    public ResponseEntity<?> handleAppException(HttpServletRequest request, HttpServletResponse response, ApiException ex) {
        log.error("[exception] uri : {} {} code : {} msg : {} {}", request.getRequestURI(), request.getMethod(), ex.getCode(), ex.getMessage(), ex.getClass(), ex);
        final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), ex.getCode());
        return new ResponseEntity<Object>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

}
