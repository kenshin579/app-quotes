package kr.pe.advenoh.quote.service;

import kr.pe.advenoh.quote.model.dto.PagedResponseDto;
import kr.pe.advenoh.quote.model.dto.QuoteRequestDto;
import kr.pe.advenoh.quote.model.dto.QuoteResponseDto;
import kr.pe.advenoh.quote.model.entity.Quote;
import kr.pe.advenoh.quote.model.entity.QuoteTagMapping;
import kr.pe.advenoh.quote.repository.quote.QuoteRepository;
import kr.pe.advenoh.quote.repository.quote.QuoteTagMappingRepository;
import kr.pe.advenoh.quote.util.DefaultSpringTestSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class QuoteServiceTest extends DefaultSpringTestSupport {
    @Autowired
    private QuoteService quoteService;

    @Autowired
    private QuoteTagMappingRepository quoteTagMappingRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    @Test
    @Transactional
//    @WithMockUser(username = "testuser", roles = "USER")
    void createQuote() {
        QuoteRequestDto quoteRequestDto = QuoteRequestDto.builder()
                .authorName("Frank")
                .quoteText("quote1")
                .tags(Arrays.asList("A", "B", "C"))
                .build();

        Principal principal = () -> "testuser";
        QuoteResponseDto quote = quoteService.createQuote(quoteRequestDto, principal);
        log.info("[quotedebug] quote : {}", quote);

        List<Quote> quotes = quoteRepository.findAll();
        assertThat(quotes.get(0).getQuoteText()).isEqualTo("quote1");

        List<QuoteTagMapping> quoteTagMappings = quoteTagMappingRepository.findAll();
        log.info("[quotedebug] quoteTagMappings : {}", quoteTagMappings);
//        assertThat(quoteTagMappings.get(0).getTag())

    }

    @Test
    @Transactional
    void getTodayQuotes() {
        PagedResponseDto<QuoteResponseDto> todayQuotes = quoteService.getTodayQuotes(0, 10);
        log.info("[quotedebug] todayQuotes : {}", todayQuotes.getContent());
    }
}