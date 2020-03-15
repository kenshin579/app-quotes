package kr.pe.advenoh.quote.exception;

public interface ExceptionCode {
    String getCode();

    String getMessage(String... args);
}
