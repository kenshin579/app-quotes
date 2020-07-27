package kr.pe.advenoh.quote.service;

import kr.pe.advenoh.quote.model.entity.Tag;
import kr.pe.advenoh.quote.repository.quote.QuoteRepository;
import kr.pe.advenoh.quote.util.MockitoTestSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class QuoteServiceMockTest extends MockitoTestSupport {

    @InjectMocks
    private QuoteService quoteService;

    @Mock
    private QuoteRepository quoteRepository;

    @Test
    void createQuote() {


    }

    @Test
    void getDiffTags() {
        List<String> allTags = Arrays.asList("A", "B", "C", "D", "E");
        List<String> dbTags = Arrays.asList("B", "E");
        List<Tag> absentTags = quoteService.getDiffTags(allTags, dbTags);

        assertThat(absentTags.stream().map(Tag::getTagName).collect(Collectors.toList())).isEqualTo(Arrays.asList("A", "C", "D"));
    }
}