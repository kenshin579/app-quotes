package kr.pe.advenoh.quote.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum QuoteExceptionCode implements ExceptionCode {
    /*
    account : 10000
    quote   : 20000
    folder  : 30000
    user    : 40000
    */

    ACCOUNT_USER_NOT_FOUND("10000", "User not found with username or email : %s"),
    ACCOUNT_USERNAME_IS_ALREADY_EXIST("10010", "Username is already exist!"),
    ACCOUNT_EMAIL_IS_ALREADY_EXIST("10020", "Email is already exist!"),
    ACCOUNT_USER_REGISTERED_SUCCESS("10030", "User registered successfully"),
    ACCOUNT_ROLE_IS_NOT_SET("10040", "Role(%s) is not set"),
    ACCOUNT_FAILED_LOGIN("10050", "id/password를 확인해주세요"),

    QUOTE_NOT_FOUND("20000", "요청하신 명언 정보를 찾을 수 없습니다"),

    FOLDER_NOT_FOUND("30000", "요청하신 폴더 정보를 찾을 수 없습니다"),

    USER_NOT_FOUND("40000", "요청하신 사용자 정보를 찾을 수 없습니다")
    ;

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
