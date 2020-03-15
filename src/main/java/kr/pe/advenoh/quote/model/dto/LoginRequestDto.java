package kr.pe.advenoh.quote.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequestDto {
    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;
}
