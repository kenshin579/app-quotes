package kr.pe.advenoh.common.exception;

public interface ExceptionCode {
    String getCode();

    String getMessage(String... args);
}
