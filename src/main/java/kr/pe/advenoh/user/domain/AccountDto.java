package kr.pe.advenoh.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AccountDto {

    @Getter
    public static class LoginRequestDto {
        @NotBlank
        private String username;

        @NotBlank
        private String password;

        @Builder
        public LoginRequestDto(@NotBlank String username, @NotBlank String password) {
            this.username = username;
            this.password = password;
        }
    }

    @Getter
    @ToString(exclude = "password")
    public static class SignUpRequestDto {
        @NotBlank
        private String name;

        @NotBlank
        @Size(min = 3, max = 15)
        private String username;

        @NotBlank
        @Email
        private String email;

        @NotBlank
        @Size(min = 6, max = 20)
        private String password;

        private boolean enabled = true;

        @Builder
        public SignUpRequestDto(String name, String username, String email, String password) {
            this.name = name;
            this.username = username;
            this.email = email;
            this.password = password;
        }

        public User toEntity() {
            return User.builder()
                    .name(name)
                    .username(username)
                    .email(email)
                    .enabled(enabled)
                    .password(Password.builder().value(password).build())
                    .build();
        }

    }
}
