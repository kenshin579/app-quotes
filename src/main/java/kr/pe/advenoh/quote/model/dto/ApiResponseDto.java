package kr.pe.advenoh.quote.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponseDto {
    private Boolean success;
    private String message;
}
