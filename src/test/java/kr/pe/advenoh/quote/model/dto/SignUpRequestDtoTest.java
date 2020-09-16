package kr.pe.advenoh.quote.model.dto;

import kr.pe.advenoh.quote.util.TestConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SignUpRequestDtoTest implements TestConfig {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private SignUpRequestDto signUpRequestDto;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void 정상요청인_경우에는_오류가_없다() {
        signUpRequestDto = SignUpRequestDto.builder()
                .name(name)
                .username(username)
                .email(email)
                .password(password)
                .build();

        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(signUpRequestDto);
        assertThat(violations.size()).isZero();
    }
}