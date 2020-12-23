package kr.pe.advenoh.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserProfileDto {
        private Long id;
        private String name;
        private String email;

        @Builder
        public UserProfileDto(Long id, String name, String email) {
            this.id = id;
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
