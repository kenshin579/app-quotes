package kr.pe.advenoh.quote.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum QuoteExceptionCode implements ExceptionCode {
    /*
    account : 10000
    quote   : 20000
    folder  : 30000
    user    : 40000
    system  : 50000
    */

    ACCOUNT_USER_NOT_FOUND("10000", "사용자 정보를 찾을 수 없습니다"),
    ACCOUNT_USERNAME_IS_ALREADY_EXIST("10010", "Username이 이미 존재합니다"),
    ACCOUNT_EMAIL_IS_ALREADY_EXIST("10020", "이메일 주소가 이미 존재합니다"),
    ACCOUNT_USER_REGISTERED_SUCCESS("10030", "사용자 등록이 완료되었습니ㄷ"),
    ACCOUNT_ROLE_NOT_FOUND("10040", "Role(%s) 정보를 찾을 수가 없습니다"),
    ACCOUNT_FAILED_LOGIN("10050", "id/password를 확인해주세요"),

    QUOTE_NOT_FOUND("20000", "요청하신 명언 정보를 찾을 수 없습니다"),

    FOLDER_NOT_FOUND("30000", "요청하신 폴더 정보를 찾을 수 없습니다"),

    USER_NOT_FOUND("40000", "요청하신 사용자 정보를 찾을 수 없습니다"),

    REQUEST_INVALID("50000", "요청하신 값이 잘못 되었습니다. 입력값을 확인해주세요")
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
