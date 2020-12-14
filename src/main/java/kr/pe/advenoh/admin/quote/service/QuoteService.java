package kr.pe.advenoh.admin.quote.service;

import kr.pe.advenoh.admin.folder.domain.Folder;
import kr.pe.advenoh.admin.folder.domain.FolderQuoteMapping;
import kr.pe.advenoh.admin.folder.domain.FolderQuoteMappingRepository;
import kr.pe.advenoh.admin.folder.domain.FolderRepository;
import kr.pe.advenoh.admin.quote.domain.Author;
import kr.pe.advenoh.admin.quote.domain.AuthorRepository;
import kr.pe.advenoh.admin.quote.domain.Quote;
import kr.pe.advenoh.admin.quote.domain.QuoteHistory;
import kr.pe.advenoh.admin.quote.domain.QuoteHistoryRepository;
import kr.pe.advenoh.admin.quote.domain.QuoteRepository;
import kr.pe.advenoh.admin.quote.domain.QuoteTagMapping;
import kr.pe.advenoh.admin.quote.domain.QuoteTagMappingRepository;
import kr.pe.advenoh.admin.quote.domain.Tag;
import kr.pe.advenoh.admin.quote.domain.TagRepository;
import kr.pe.advenoh.admin.quote.domain.QuoteDto;
import kr.pe.advenoh.common.dto.PagedResponseDto;
import kr.pe.advenoh.common.exception.ApiException;
import kr.pe.advenoh.common.exception.QuoteExceptionCode;
import kr.pe.advenoh.user.domain.User;
import kr.pe.advenoh.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuoteService {
    //todo : repository가 너무 많음 - 개선할 수 있는 방법은?
    private final QuoteRepository quoteRepository;

    private final AuthorRepository authorRepository;

    private final UserRepository userRepository;

    private final TagRepository tagRepository;

    private final QuoteTagMappingRepository quoteTagMappingRepository;

    private final QuoteHistoryRepository quoteHistoryRepository;

    private final FolderRepository folderRepository;

    private final FolderQuoteMappingRepository folderQuoteMappingRepository;

    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public PagedResponseDto<QuoteDto.QuoteResponse> getQuotes(Long folderId, Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC, "createDt");

        Page<QuoteDto.QuoteResponse> quotes = quoteRepository.findAllByFolderId(folderId, pageable);

        if (quotes.getNumberOfElements() == 0) {
            return new PagedResponseDto<>(Collections.emptyList(), quotes.getNumber() + 1,
                    quotes.getSize(), quotes.getTotalElements(), quotes.getTotalPages(), quotes.isLast());
        }

        List<QuoteDto.QuoteResponse> quoteResponseList = quotes.getContent().stream().map(it -> {
            QuoteDto.QuoteResponse quoteResponseDto = modelMapper.map(it, QuoteDto.QuoteResponse.class);
            return quoteResponseDto;
        }).collect(Collectors.toList());

        return new PagedResponseDto<>(quoteResponseList, quotes.getNumber() + 1,
                quotes.getSize(), quotes.getTotalElements(), quotes.getTotalPages(), quotes.isLast());
    }

    //todo : 좋아요 & 공유수에 대한 정보도 내려주면 좋을 듯함
    @Transactional(readOnly = true)
    public QuoteDto.QuoteResponse getQuote(Long quoteId) {
        //todo: httpStatu를 NOT_FOUND로 반환하는게 좋아보임
        QuoteDto.QuoteResponse quoteResponseDto = quoteRepository.findAllByQuoteId(quoteId).orElseThrow(() -> new ApiException(QuoteExceptionCode.QUOTE_NOT_FOUND));
        return quoteResponseDto;
    }

    @Transactional
    public QuoteDto.QuoteResponse createQuote(Long folderId, QuoteDto.QuoteRequest quoteRequestDto, Principal currentUser) {
        log.info("[quotedebug] currentUser : {}", currentUser.getName());
        Author author = authorRepository.getAuthorByName(quoteRequestDto.getAuthorName()).orElse(new Author(quoteRequestDto.getAuthorName()));
        User user = userRepository.findByUsername(currentUser.getName()).orElseThrow(() -> new ApiException(QuoteExceptionCode.USER_NOT_FOUND));

        //todo : script 저장을 위해 tags 없이도 저장 가능하도록 변경함
        List<Tag> dbTagsEntity = null;

        if (quoteRequestDto.getTags() != null) {
            dbTagsEntity = tagRepository.findByTagNameIn(quoteRequestDto.getTags());
            List<String> dbTags = dbTagsEntity.stream().map(Tag::getTagName).collect(Collectors.toList());
            List<Tag> diffTags = this.getDiffTags(quoteRequestDto.getTags(), dbTags);
            tagRepository.saveAll(diffTags);
            dbTagsEntity.addAll(diffTags);

            log.debug("[quotedebug] diffTags : {}", diffTags);
        }

        //todo : author, user를 어떻게 넘길 것인가?
//        Quote quote = Quote.builder()
//                .quoteText(quoteRequestDto.getQuoteText())
//                .useYn(quoteRequestDto.getUseYn())
//                .author(author)
//                .user(user)
//                .build();

        Quote quote = quoteRequestDto.toEntity();

        quoteRepository.save(quote);
        QuoteDto.QuoteResponse quoteResponseDto = modelMapper.map(quote, QuoteDto.QuoteResponse.class);

        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new ApiException(QuoteExceptionCode.FOLDER_NOT_FOUND));

        folderQuoteMappingRepository.save(new FolderQuoteMapping(folder, quote));

        if (quoteRequestDto.getTags() != null) {
            quoteTagMappingRepository.saveAll(dbTagsEntity.stream().map(tagEntity -> new QuoteTagMapping(quote, tagEntity)).collect(Collectors.toList()));
            quoteResponseDto.setTags(dbTagsEntity.stream().map(Tag::getTagName).collect(Collectors.toList()));
        }
        return quoteResponseDto;
    }

    @Transactional
    public QuoteDto.QuoteResponse updateQuote(Long quoteId, QuoteDto.QuoteRequest quoteRequestDto) {
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(() -> new ApiException(QuoteExceptionCode.QUOTE_NOT_FOUND));

        Optional.ofNullable(quoteRequestDto.getQuoteText()).ifPresent(quote::setQuoteText);
        Optional.ofNullable(quoteRequestDto.getUseYn()).ifPresent(quote::setUseYn);

        Author author = authorRepository.getAuthorByName(quoteRequestDto.getAuthorName()).orElse(new Author(quoteRequestDto.getAuthorName()));
        quote.setAuthor(author);

        List<Tag> dbTagsEntity = tagRepository.findByTagNameIn(quoteRequestDto.getTags());
        List<String> dbTags = dbTagsEntity.stream().map(Tag::getTagName).collect(Collectors.toList());
        List<Tag> diffTags = this.getDiffTags(quoteRequestDto.getTags(), dbTags);
        log.info("[quotedebug] diffTags: {}", diffTags);
        tagRepository.saveAll(diffTags);
        dbTagsEntity.addAll(diffTags);

        quoteRepository.save(quote);

        QuoteDto.QuoteResponse quoteResponseDto = modelMapper.map(quote, QuoteDto.QuoteResponse.class);

        //todo: 기존 매핑을 삭제해야 함
        quoteTagMappingRepository.deleteAllByQuoteIds(Arrays.asList(quote.getId()));

        quoteTagMappingRepository.saveAll(dbTagsEntity.stream().map(tagEntity -> new QuoteTagMapping(quote, tagEntity)).collect(Collectors.toList()));
        quoteResponseDto.setTags(dbTagsEntity.stream().map(Tag::getTagName).collect(Collectors.toList()));
        return quoteResponseDto;
    }

    @Transactional
    public Integer deleteQuotes(List<Long> quoteIds) {
        //todo : 삭제 호출이 안되는 이슈가 있음 (해당 폴더에서 명언을 삭제해야 함)
        //todo : 명언 삭제시 다른 table로 이동하고 batch 작업으로 migratino 하는 작업이 별도로 필요함
        quoteTagMappingRepository.deleteAllByQuoteIds(quoteIds);
        folderQuoteMappingRepository.deleteByQuoteIds(quoteIds);
        quoteHistoryRepository.deleteAllByQuoteIds(quoteIds);
        return quoteRepository.deleteAllByQuoteIds(quoteIds);
    }

    @Transactional
    public boolean moveQuotes(List<Long> quoteIds, Long folderId) {
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new ApiException(QuoteExceptionCode.FOLDER_NOT_FOUND));

        List<FolderQuoteMapping> folderQuoteMappings = folderQuoteMappingRepository.findAllById(quoteIds);
        folderQuoteMappings.forEach(it -> it.setFolder(folder));
        return true;
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<QuoteDto.QuoteResponse> getTodayQuotes(Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC, "createDt");
        Page<QuoteHistory> quoteHistories = quoteHistoryRepository.findAll(pageable);

        log.info("[quotedebug] quoteHistories : {}", quoteHistories.getContent());

        if (quoteHistories.getNumberOfElements() == 0) {
            return new PagedResponseDto<>(Collections.emptyList(), quoteHistories.getNumber(),
                    quoteHistories.getSize(), quoteHistories.getTotalElements(), quoteHistories.getTotalPages(), quoteHistories.isLast());
        }

        //todo : 이거 QuoteMain 페이지 작업할 때 다시 하는 걸로 함 (quote 삭제할 때 이슈가 있음)
        List<QuoteDto.QuoteResponse> quoteResponseList = quoteHistories.getContent().stream().map(quoteHistory -> {
            //todo : modelMapper으로 어떻게 하면 되는지 다시 확인하기
//            QuoteDto.QuoteResponse quoteResponseDto = modelMapper.map(quoteHistory, QuoteDto.QuoteResponse.class);
            QuoteDto.QuoteResponse quoteResponseDto = QuoteDto.QuoteResponse.builder()
                    .quoteId(quoteHistory.getQuote().getId())
                    .quoteText(quoteHistory.getQuote().getQuoteText())
                    .authorName(quoteHistory.getQuote().getAuthor().getName())
                    .build();

            return quoteResponseDto;
        }).collect(Collectors.toList());

        return new PagedResponseDto<>(quoteResponseList, quoteHistories.getNumber() + 1,
                quoteHistories.getSize(), quoteHistories.getTotalElements(), quoteHistories.getTotalPages(), quoteHistories.isLast());
    }

    /**
     * todo : randomness를 테스트할 필요가 있음
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Quote getRandomQuote() {
        return quoteRepository.getRandomQuote();
    }

    @Transactional
    public QuoteHistory saveRandomQuote() {
        Quote quote = this.getRandomQuote();
        return quoteHistoryRepository.save(new QuoteHistory(quote));
    }

    /**
     * todo : jpa like로 개선 작업하기
     * 명언의 한 부분이 겹치는 경우에는 DB에 있다고 판단하는게 좋아보임
     *
     * @param quoteText
     * @return
     */
    @Transactional(readOnly = true)
    public boolean doesQuoteExists(String quoteText) {
        return quoteRepository.existsQuoteByQuoteText(quoteText);
    }

    /**
     * allTags - dbTags의 태그를 반환함
     *
     * @param allTags the allTags
     * @param dbTags the dbTags
     * @return List
     */
    public List<Tag> getDiffTags(List<String> allTags, List<String> dbTags) {
        return allTags.stream()
                .filter(it -> !dbTags.contains(it))
                .map(Tag::new)
                .collect(Collectors.toList());
    }
}
