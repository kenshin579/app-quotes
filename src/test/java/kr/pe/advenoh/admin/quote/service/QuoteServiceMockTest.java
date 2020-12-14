package kr.pe.advenoh.admin.quote.service;

import kr.pe.advenoh.admin.folder.domain.Folder;
import kr.pe.advenoh.admin.folder.domain.FolderQuoteMappingRepository;
import kr.pe.advenoh.admin.folder.domain.FolderRepository;
import kr.pe.advenoh.admin.quote.domain.Author;
import kr.pe.advenoh.admin.quote.domain.AuthorRepository;
import kr.pe.advenoh.admin.quote.domain.Quote;
import kr.pe.advenoh.admin.quote.domain.QuoteRepository;
import kr.pe.advenoh.admin.quote.domain.Tag;
import kr.pe.advenoh.admin.quote.domain.TagRepository;
import kr.pe.advenoh.admin.quote.domain.QuoteDto;
import kr.pe.advenoh.user.domain.User;
import kr.pe.advenoh.user.domain.UserRepository;
import kr.pe.advenoh.util.MockitoTestSupport;
import kr.pe.advenoh.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
class QuoteServiceMockTest extends MockitoTestSupport {

    @InjectMocks
    private QuoteService quoteService;

    @Mock
    private QuoteRepository quoteRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private FolderQuoteMappingRepository folderQuoteMappingRepository;

    @Spy
    private ModelMapper modelMapper;

    @Captor
    private ArgumentCaptor<List<Tag>> argumentCaptorForListOfTag;

    private String prefixStr;

    @BeforeEach
    void setUp() {
        prefixStr = TestUtils.generateRandomString(4);
    }

    //todo : entity 수정시 다시 작업하는 걸로 함
    @Test
    @Disabled
    void createQuote_tags_모두_새로운_태그인_경우() {
        //given
        List<String> tags = Arrays.asList(prefixStr + "1", prefixStr + "2", prefixStr + "3");
        QuoteDto.QuoteRequest quoteRequestDto = QuoteDto.QuoteRequest.builder()
                .authorName(authorName)
                .quoteText(quoteText)
                .tags(tags)
                .build();

        Principal principal = () -> username;

        when(authorRepository.getAuthorByName(authorName)).thenReturn(Optional.of(new Author(authorName)));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(User.builder().name(name).build()));
        when(tagRepository.findByTagNameIn(any())).thenReturn(new ArrayList<>());
        when(tagRepository.saveAll(any())).thenReturn(new ArrayList<>());
        when(folderRepository.findById(anyLong())).thenReturn(Optional.of(new Folder(folderName)));
        when(folderQuoteMappingRepository.save(any())).thenReturn(null);

        //when
        QuoteDto.QuoteResponse quote = quoteService.createQuote(folderId, quoteRequestDto, principal);
        log.info("[quotedebug] quote : {}", quote);

        //then
        verify(authorRepository).getAuthorByName(authorName);
        verify(userRepository).findByUsername(username);
        verify(tagRepository).findByTagNameIn(any());
        verify(tagRepository).saveAll(argumentCaptorForListOfTag.capture());
        assertThat(argumentCaptorForListOfTag.getValue().size()).isEqualTo(tags.size());

//        List<Quote> quotes = quoteRepository.findAll();
//        assertThat(quotes.get(0).getQuoteText()).isEqualTo("quote1");

//        List<QuoteTagMapping> quoteTagMappings = quoteTagMappingRepository.findAll();
//        log.info("[quotedebug] quoteTagMappings : {}", quoteTagMappings);
//        assertThat(quoteTagMappings.get(0).getTag())

    }

    //todo : entity 수정시 다시 작업하는 걸로 함
    @Test
    @Disabled
    void createQuote_tags_새로운_태그_2개_이미_존재하는_태그_1개_인_경우() {
        QuoteDto.QuoteRequest quoteRequestDto = QuoteDto.QuoteRequest.builder()
                .authorName("Frank")
                .quoteText("quote1")
                .tags(Arrays.asList("A", "B", "C"))
                .build();

        Principal principal = () -> "testuser";
        QuoteDto.QuoteResponse quote = quoteService.createQuote(folderId, quoteRequestDto, principal);
        log.info("[quotedebug] quote : {}", quote);

        List<Quote> quotes = quoteRepository.findAll();
        assertThat(quotes.get(0).getQuoteText()).isEqualTo("quote1");

//        List<QuoteTagMapping> quoteTagMappings = quoteTagMappingRepository.findAll();
//        log.info("[quotedebug] quoteTagMappings : {}", quoteTagMappings);
//        assertThat(quoteTagMappings.get(0).getTag())

    }

    @Test
    void getDiffTags() {
        List<String> allTags = Arrays.asList("A", "B", "C", "D", "E");
        List<String> dbTags = Arrays.asList("B", "E");
        List<Tag> absentTags = quoteService.getDiffTags(allTags, dbTags);

        assertThat(absentTags.stream().map(Tag::getTagName).collect(Collectors.toList())).isEqualTo(Arrays.asList("A", "C", "D"));
    }
}