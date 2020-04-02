package kr.pe.advenoh.quote.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoDto {
    private Long id;
    private String username;
    private String name;
}
