package kr.pe.advenoh.quote.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString(exclude = "password")
public class SignUpRequestDto {
    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
}
