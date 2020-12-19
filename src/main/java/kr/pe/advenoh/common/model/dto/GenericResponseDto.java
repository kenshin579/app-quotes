package kr.pe.advenoh.common.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Slf4j
public class GenericResponseDto {
    private String message;
    private String error;

    public GenericResponseDto(final String message) {
        super();
        this.message = message;
    }

    public GenericResponseDto(final String message, final String error) {
        super();
        this.message = message;
        this.error = error;
    }

    public GenericResponseDto(List<ObjectError> allErrors, String error) {
        this.error = error;
        String temp = allErrors.stream().map(e -> {
            if (e instanceof FieldError) {
                return "{\"field\":\"" + ((FieldError) e).getField() + "\",\"defaultMessage\":\"" + e.getDefaultMessage() + "\"}";
            } else {
                return "{\"object\":\"" + e.getObjectName() + "\",\"defaultMessage\":\"" + e.getDefaultMessage() + "\"}";
            }
        }).collect(Collectors.joining(","));
        this.message = "[" + temp + "]";
        log.info("[FRANK] message : {}", message);
    }
}
