package kr.pe.advenoh.quote.service;

import kr.pe.advenoh.quote.model.entity.Tag;
import kr.pe.advenoh.quote.repository.quote.QuoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class QuoteServiceMockTest {

    @InjectMocks
    private QuoteService quoteService;

    @Mock
    private QuoteRepository quoteRepository;

    @Test
    public void createQuote() {


    }

    @Test
    public void getDiffTags() {
        List<String> allTags = Arrays.asList("A", "B", "C", "D", "E");
        List<String> dbTags = Arrays.asList("B", "E");
        List<Tag> absentTags = quoteService.getDiffTags(allTags, dbTags);

        assertThat(absentTags.stream().map(Tag::getTagName).collect(Collectors.toList())).isEqualTo(Arrays.asList("A", "C", "D"));
    }
}