package kr.pe.advenoh.quote.web.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum QuoteExceptionCode implements ExceptionCode {
    /*
    account : 10000

    */

    ACCOUNT_USER_NOT_FOUND("10000", "User not found with username or email : %s"),
    ACCOUNT_USERNAME_IS_ALREADY_EXIST("10001", "Username is already exist!"),
    ACCOUNT_EMAIL_IS_ALREADY_EXIST("10002", "Email is already exist!"),
    ACCOUNT_USER_REGISTERED_SUCCESS("10003", "User registered successfully");
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
