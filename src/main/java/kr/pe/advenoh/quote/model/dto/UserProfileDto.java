package kr.pe.advenoh.quote.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileDto {
    private Long id;
    private String username;
    private String name;
    private String email;
}
