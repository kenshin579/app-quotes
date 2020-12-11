package kr.pe.advenoh.admin.quote.service;

import kr.pe.advenoh.admin.quote.domain.Quote;
import kr.pe.advenoh.admin.quote.domain.QuoteHistory;
import kr.pe.advenoh.admin.quote.domain.QuoteHistoryRepository;
import kr.pe.advenoh.util.SpringBootTestSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class QuoteServiceTest extends SpringBootTestSupport {
    @Autowired
    private QuoteService quoteService;

    @Autowired
    private QuoteHistoryRepository quoteHistoryRepository;

    @Test
    @Disabled
    @Transactional
    void deleteQuotes_quoteHistory에도_있어도_삭제되어야_한다() {
        //given
        Quote randomQuote = quoteService.getRandomQuote();
        quoteHistoryRepository.save(new QuoteHistory(randomQuote));

        //when
        Integer count = quoteService.deleteQuotes(Arrays.asList(randomQuote.getId()));

        //then
        assertThat(count).isGreaterThanOrEqualTo(1);
    }
}