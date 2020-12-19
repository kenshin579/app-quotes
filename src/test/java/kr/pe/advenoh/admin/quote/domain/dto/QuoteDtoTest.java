package kr.pe.advenoh.admin.quote.domain.dto;

import kr.pe.advenoh.admin.quote.domain.QuoteDto;
import kr.pe.advenoh.admin.quote.domain.enums.YN;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class QuoteDtoTest {
	private static ValidatorFactory validatorFactory;
	private static Validator validator;
	private QuoteDto.QuoteRequest quoteRequestDto;

	@BeforeEach
	void setUp() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@Test
	void 정상요청인_경우에는_오류가_없다() {
		quoteRequestDto = QuoteDto.QuoteRequest.builder()
				.authorName("frank")
				.useYn(YN.Y)
				.quoteText("quote text").build();
		Set<ConstraintViolation<QuoteDto.QuoteRequest>> violations = validator.validate(quoteRequestDto);
		assertThat(violations.size()).isZero();
	}

	@Test
	void 요청값이_null인_경우() {
		quoteRequestDto = QuoteDto.QuoteRequest.builder().build();
		Set<ConstraintViolation<QuoteDto.QuoteRequest>> violations = validator.validate(quoteRequestDto);
		assertThat(violations.size()).isEqualTo(2);
		assertThat(violations.iterator().next().getMessage()).isEqualTo("반드시 값이 있어야 합니다.");
	}

	@Test
	void 요청값이_quoteText를_세팅하지_않는_경우() {
		quoteRequestDto = QuoteDto.QuoteRequest.builder()
				.useYn(YN.Y).build();
		Set<ConstraintViolation<QuoteDto.QuoteRequest>> violations = validator.validate(quoteRequestDto);
		assertThat(violations.size()).isEqualTo(1);
		assertThat(violations.iterator().next().getMessage()).isEqualTo("반드시 값이 있어야 합니다.");
	}

}