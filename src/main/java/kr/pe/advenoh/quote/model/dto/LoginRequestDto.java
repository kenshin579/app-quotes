package kr.pe.advenoh.quote.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class LoginRequestDto {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
