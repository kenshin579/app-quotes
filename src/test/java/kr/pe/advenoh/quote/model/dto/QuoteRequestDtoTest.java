package kr.pe.advenoh.quote.model.dto;

import kr.pe.advenoh.quote.model.enums.YN;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class QuoteRequestDtoTest {
	private static ValidatorFactory validatorFactory;
	private static Validator validator;
	private QuoteRequestDto quoteRequestDto;

	@BeforeEach
	void setUp() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@Test
	void 정상요청인_경우에는_오류가_없다() {
		quoteRequestDto = QuoteRequestDto.builder()
				.authorName("frank")
				.useYn(YN.Y)
				.quoteText("quote text").build();
		Set<ConstraintViolation<QuoteRequestDto>> violations = validator.validate(quoteRequestDto);
		assertThat(violations.size()).isZero();
	}

	@Test
	void 요청값이_null인_경우() {
		quoteRequestDto = QuoteRequestDto.builder().build();
		Set<ConstraintViolation<QuoteRequestDto>> violations = validator.validate(quoteRequestDto);
		assertThat(violations.size()).isEqualTo(2);
		assertThat(violations.iterator().next().getMessage()).isEqualTo("반드시 값이 있어야 합니다.");
	}

	@Test
	void 요청값이_quoteText를_세팅하지_않는_경우() {
		quoteRequestDto = QuoteRequestDto.builder()
				.useYn(YN.Y).build();
		Set<ConstraintViolation<QuoteRequestDto>> violations = validator.validate(quoteRequestDto);
		assertThat(violations.size()).isEqualTo(1);
		assertThat(violations.iterator().next().getMessage()).isEqualTo("반드시 값이 있어야 합니다.");
	}

}