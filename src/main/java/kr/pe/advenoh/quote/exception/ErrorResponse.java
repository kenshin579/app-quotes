package kr.pe.advenoh.quote.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private HttpStatus status;
    private String message;
    private String code;
    private List<String> details;

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorResponse(HttpStatus status, String message, String code) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
