package kr.pe.advenoh.quote.service;

import kr.pe.advenoh.quote.model.dto.PagedResponseDto;
import kr.pe.advenoh.quote.model.dto.QuoteRequestDto;
import kr.pe.advenoh.quote.model.dto.QuoteResponseDto;
import kr.pe.advenoh.quote.model.entity.Author;
import kr.pe.advenoh.quote.model.entity.Folder;
import kr.pe.advenoh.quote.model.entity.FolderQuoteMapping;
import kr.pe.advenoh.quote.model.entity.Quote;
import kr.pe.advenoh.quote.model.entity.QuoteHistory;
import kr.pe.advenoh.quote.model.entity.QuoteTagMapping;
import kr.pe.advenoh.quote.model.entity.Tag;
import kr.pe.advenoh.quote.model.entity.User;
import kr.pe.advenoh.quote.repository.AuthorRepository;
import kr.pe.advenoh.quote.repository.FolderQuoteMappingRepository;
import kr.pe.advenoh.quote.repository.FolderRepository;
import kr.pe.advenoh.quote.repository.QuoteHistoryRepository;
import kr.pe.advenoh.quote.repository.QuoteRepository;
import kr.pe.advenoh.quote.repository.QuoteTagMappingRepository;
import kr.pe.advenoh.quote.repository.TagRepository;
import kr.pe.advenoh.quote.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuoteService {
    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private QuoteTagMappingRepository quoteTagMappingRepository;

    @Autowired
    private QuoteHistoryRepository quoteHistoryRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FolderQuoteMappingRepository folderQuoteMappingRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public QuoteResponseDto createQuote(QuoteRequestDto quoteRequestDto, Principal currentUser) {
        log.info("[quotedebug] currentUser : {}", currentUser.getName());
        Author author = authorRepository.getAuthorByName(quoteRequestDto.getAuthorName()).orElse(new Author(quoteRequestDto.getAuthorName()));
        User user = userRepository.findByUsername(currentUser.getName()).orElseThrow(() -> {
            throw new RuntimeException("not found - user");
        });

        List<Tag> dbTagsEntity = tagRepository.findByTagNameIn(quoteRequestDto.getTags());
        List<String> dbTags = dbTagsEntity.stream().map(Tag::getTagName).collect(Collectors.toList());
        List<Tag> absentTags = this.getAbsentTags(quoteRequestDto.getTags(), dbTags);
        tagRepository.saveAll(absentTags);
        dbTagsEntity.addAll(absentTags);

        Quote quote = Quote.builder()
                .quoteText(quoteRequestDto.getQuoteText())
                .useYn(quoteRequestDto.getUseYn())
                .author(author)
                .user(user)
                .build();

        quoteRepository.save(quote);
        QuoteResponseDto quoteResponseDto = modelMapper.map(quote, QuoteResponseDto.class);

        Folder folder = folderRepository.findById(quoteRequestDto.getFolderId()).orElseThrow(() -> {
            throw new RuntimeException("not found - folder");
        });

        folderQuoteMappingRepository.save(new FolderQuoteMapping(folder, quote));

        List<QuoteTagMapping> quoteTagMappings = dbTagsEntity.stream().map(tagEntity -> new QuoteTagMapping(quote, tagEntity)).collect(Collectors.toList());
        quoteTagMappingRepository.saveAll(quoteTagMappings);
        return quoteResponseDto;
    }


    @Transactional
    public Object updateQuote(Long quoteId, QuoteRequestDto quoteRequestDto, Principal currentUser) {

        return null;
    }

    //todo : 좋아요 & 공유수에 대한 정보도 내려주면 좋을 듯함
    public QuoteResponseDto getQuote(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(() -> {
            throw new RuntimeException("not found");
        });

        QuoteResponseDto quoteResponseDto = modelMapper.map(quote, QuoteResponseDto.class);
        return quoteResponseDto;
    }

    public PagedResponseDto<QuoteResponseDto> getQuotes(Long folderId, Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC, "createDt");

        Page<Quote> quotes = quoteRepository.findAllByFolderId(folderId, pageable);

        if (quotes.getNumberOfElements() == 0) {
            return new PagedResponseDto<>(Collections.emptyList(), quotes.getNumber() + 1,
                    quotes.getSize(), quotes.getTotalElements(), quotes.getTotalPages(), quotes.isLast());
        }

        List<QuoteResponseDto> quoteResponseList = quotes.getContent().stream().map(it -> {
            QuoteResponseDto quoteResponseDto = modelMapper.map(it, QuoteResponseDto.class);
            return quoteResponseDto;
        }).collect(Collectors.toList());

        return new PagedResponseDto<>(quoteResponseList, quotes.getNumber() + 1,
                quotes.getSize(), quotes.getTotalElements(), quotes.getTotalPages(), quotes.isLast());
    }

    @Transactional
    public Integer deleteQuotes(List<Long> quoteIds) {
        //todo : 삭제 호출이 안되는 이슈가 있음 (해당 폴더에서 명언을 삭제해야 함)
        quoteTagMappingRepository.deleteAllByIdInQuery(quoteIds);
        folderQuoteMappingRepository.deleteByQuoteIdQuery(quoteIds);
        return quoteRepository.deleteAllByIdInQuery(quoteIds);
    }

    public PagedResponseDto<QuoteResponseDto> getTodayQuotes(Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC, "createDt");
        Page<QuoteHistory> quoteHistories = quoteHistoryRepository.findAll(pageable);

        log.info("[quotedebug] quoteHistories : {}", quoteHistories.getContent());

        if (quoteHistories.getNumberOfElements() == 0) {
            return new PagedResponseDto<>(Collections.emptyList(), quoteHistories.getNumber(),
                    quoteHistories.getSize(), quoteHistories.getTotalElements(), quoteHistories.getTotalPages(), quoteHistories.isLast());
        }

        //todo : 이거 QuoteMain 페이지 작업할 때 다시 하는 걸로 함 (quote 삭제할 때 이슈가 있음)
        List<QuoteResponseDto> quoteResponseList = quoteHistories.getContent().stream().map(quoteHistory -> {
            //todo : modelMapper으로 어떻게 하면 되는지 다시 확인하기
//            QuoteResponseDto quoteResponseDto = modelMapper.map(quoteHistory, QuoteResponseDto.class);
            QuoteResponseDto quoteResponseDto = QuoteResponseDto.builder()
                    .quoteId(quoteHistory.getQuote().getId())
                    .quoteText(quoteHistory.getQuote().getQuoteText())
                    .authorName(quoteHistory.getQuote().getAuthor().getName())
                    .build();

            return quoteResponseDto;
        }).collect(Collectors.toList());

        return new PagedResponseDto<>(quoteResponseList, quoteHistories.getNumber(),
                quoteHistories.getSize(), quoteHistories.getTotalElements(), quoteHistories.getTotalPages(), quoteHistories.isLast());
    }

    /**
     * todo : randomness를 테스트할 필요가 있음
     *
     * @return
     */
    public QuoteResponseDto getRandomQuote() {
        Quote quote = quoteRepository.getRandomQuote();
        return modelMapper.map(quote, QuoteResponseDto.class);
    }

    @Transactional
    public QuoteHistory saveRandomQuote() {
        Quote quote = quoteRepository.getRandomQuote();
        return quoteHistoryRepository.save(new QuoteHistory(quote));
    }

    protected List<Tag> getAbsentTags(List<String> allTags, List<String> dbTags) {
        return allTags.stream()
                .filter(it -> !dbTags.contains(it))
                .map(Tag::new)
                .collect(Collectors.toList());
    }
}
