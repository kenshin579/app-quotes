package kr.pe.advenoh.quote.service;

import kr.pe.advenoh.quote.model.entity.Quote;
import kr.pe.advenoh.quote.model.entity.QuoteHistory;
import kr.pe.advenoh.quote.repository.quote.QuoteHistoryRepository;
import kr.pe.advenoh.quote.util.SpringBootTestSupport;
import lombok.extern.slf4j.Slf4j;
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