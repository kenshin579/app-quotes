package kr.pe.advenoh.quote.web.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum QuoteExceptionCode implements ExceptionCode {
    /*
    account : 10000

    */

    ACCOUNT_USER_NOT_FOUND("10000", "User not found with username or email : %s");
    private String code;
    private String message;

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage(String... args) {
        return String.format(this.message, args);
    }
}
