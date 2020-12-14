package kr.pe.advenoh.user.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
        private String email;

        @NotBlank
        @Size(min = 6, max = 20)
        private String password;

        @Builder
        public SignUpRequestDto(String name, String username, String email, String password) {
            this.name = name;
            this.username = username;
            this.email = email;
            this.password = password;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class UserProfileDto {
        private Long id;
        private String username;
        private String name;
        private String email;

        @Builder
        public UserProfileDto(Long id, String username, String name, String email) {
            this.id = id;
            this.username = username;
            this.name = name;
            this.email = email;
        }
    }

    @Getter
    public static class UserResponseDto {
        private Long id;
        private String username;
        private String name;

        @Builder
        public UserResponseDto(Long id, String username, String name) {
            this.id = id;
            this.username = username;
            this.name = name;
        }
    }
}
