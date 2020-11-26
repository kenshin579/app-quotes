package kr.pe.advenoh.user.domain.dto;

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
