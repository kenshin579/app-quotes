package kr.pe.advenoh.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {

    @Column(name = "password", nullable = false)
    private String value;

    @Builder
    public Password(final String value) {
        this.value = encodePassword(value);
    }

    public boolean isMatched(final String rawPassword) {
        final boolean matches = isMatches(rawPassword);
        return matches;
    }

    public void changePassword(final String newPassword, final String oldPassword) {
        if (isMatched(oldPassword)) {
            value = encodePassword(newPassword);
        }
    }

    private String encodePassword(final String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    private boolean isMatches(String rawPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, this.value);
    }

}
