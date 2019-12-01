package kr.pe.advenoh.quote.web.exception;

public interface ExceptionCode {
    String getCode();

    String getMessage(String... args);
}
