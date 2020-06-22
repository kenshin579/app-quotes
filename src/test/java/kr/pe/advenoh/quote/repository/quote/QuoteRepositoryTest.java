package kr.pe.advenoh.quote.repository.quote;

import kr.pe.advenoh.quote.model.dto.QuoteResponseDto;
import kr.pe.advenoh.quote.model.entity.Author;
import kr.pe.advenoh.quote.model.entity.Quote;
import kr.pe.advenoh.quote.model.enums.YN;
import kr.pe.advenoh.quote.repository.AuthorRepository;
import kr.pe.advenoh.quote.repository.quote.QuoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
//@Import({JpaConfig.class, AuditingConfig.class})
//@DataJpaTest
//@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class QuoteRepositoryTest {

    @Autowired
    private QuoteRepository quoteRepository;

//    @Autowired
//    private UserRepository userRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    public void quote_save() {
        Author saveAuthor = authorRepository.save(new Author("frank1"));
        Quote quote = Quote.builder()
                .quoteText("quote1")
                .useYn(YN.N)
                .author(saveAuthor)
                .build();

        Quote saveQuote = quoteRepository.save(quote);
        log.info("saveQuote : {}", saveQuote);

        List<Quote> quotes = quoteRepository.findAll();
        assertThat(quotes.get(0).getQuoteText()).isEqualTo("quote1");
        assertThat(quotes.get(0).getUseYn()).isEqualTo(YN.N);

        assertThat(quotes.get(0).getAuthor().getId()).isEqualTo(saveAuthor.getId());
    }

    @Test
    @Transactional
    public void findAllByFolderId() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createDt");
        Long folderId = 6L;
        Page<QuoteResponseDto> quotes = quoteRepository.findAllByFolderId(folderId, pageable);
        List<QuoteResponseDto> content = quotes.getContent();

        assertThat(content.size()).isNotZero();

        //todo : new Comparator로 작성하기
        if (content.size() > 2) {
            assertThat(content.get(0).getQuoteId()).isNotEqualTo(content.get(1).getQuoteId());
        }

        assertThat(content.stream().map(QuoteResponseDto::getQuoteId).collect(Collectors.toList()))
                .isSortedAccordingTo(Comparator.reverseOrder());

        //체크 tags (중복이 없어야 함)
        content.stream().forEach(x -> {
            boolean duplicated = x.getTags().stream()
                    .distinct()
                    .count() != x.getTags().size();
            assertThat(duplicated).isFalse();
        });

        log.info("QuoteResponseDto : {}", quotes.getContent());
    }

    @Test
    public void findAllByQuoteId() {
        QuoteResponseDto quoteResponseDto = quoteRepository.findAllByQuoteId(60L).get();
        log.info("quoteResponseDto : {}", quoteResponseDto);
    }
}