package kr.pe.advenoh.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
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
    /*
    todo : Request에 대한 validation 추가시 예외 처리 (MethodArgumentNotValidException)
    여러 fields에 대한 오류는 errors: []로 넣어서 내려주기
    https://github.com/cheese10yun/spring-jpa-best-practices/blob/master/doc/step-02.md
     */

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseEntity<?> handleMethodArgumentNotValidExceptionException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, QuoteExceptionCode.REQUEST_INVALID.getMessage(),
                QuoteExceptionCode.REQUEST_INVALID.getCode(), details);
        return new ResponseEntity<Object>(error, error.getStatus());
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseEntity<?> handleException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error("[exception] uri : {} {} msg : {} {}", request.getRequestURI(), request.getMethod(), ex.getMessage(), ex.getClass(), ex);
        final ErrorResponse errorReponse = new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
        return new ResponseEntity<Object>(errorReponse, new HttpHeaders(), errorReponse.getStatus());
    }

    @ExceptionHandler({ApiException.class})
    @ResponseBody
    public ResponseEntity<?> handleAppException(HttpServletRequest request, HttpServletResponse response, ApiException ex) {
        log.error("[exception] uri : {} {} code : {} msg : {} {}", request.getRequestURI(), request.getMethod(), ex.getCode(), ex.getMessage(), ex.getClass(), ex);
        final ErrorResponse errorReponse = new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), ex.getCode());
        return new ResponseEntity<Object>(errorReponse, new HttpHeaders(), errorReponse.getStatus());
    }
}
