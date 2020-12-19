package kr.pe.advenoh.user.domain;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationResponseDto {
    private String TOKEN_TYPE = "Bearer";
    private String accessToken;

    public JwtAuthenticationResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
