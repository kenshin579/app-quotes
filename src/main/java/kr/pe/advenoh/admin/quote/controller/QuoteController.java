package kr.pe.advenoh.admin.quote.controller;

import kr.pe.advenoh.admin.quote.domain.dto.QuoteDto;
import kr.pe.advenoh.admin.quote.domain.dto.QuoteDto.QuoteResponse;
import kr.pe.advenoh.admin.quote.service.QuoteLikeService;
import kr.pe.advenoh.admin.quote.service.QuoteService;
import kr.pe.advenoh.common.constants.AppConstants;
import kr.pe.advenoh.common.exception.ApiException;
import kr.pe.advenoh.common.exception.QuoteExceptionCode;
import kr.pe.advenoh.spring.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

    private final QuoteService quoteService;

    private final QuoteLikeService quoteLikeService;

    private final ModelMapper modelMapper;

    @GetMapping("/folders/{folderId}")
    public ResponseEntity<?> getQuotes(
            @PathVariable(name = "folderId") Long folderId,
            @RequestParam(value = "pageIndex", defaultValue = AppConstants.DEFAULT_PAGE_INDEX) Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer pageSize) {
        return new ResponseEntity<>(quoteService.getQuotes(folderId, pageIndex, pageSize), HttpStatus.OK);
    }

    //todo : 좋아요 정보를 같이 내려주는 게 좋아보임
    @GetMapping("/{quoteId}")
    public ResponseEntity<?> getQuote(@PathVariable(name = "quoteId") Long quoteId) {
        return new ResponseEntity<>(quoteService.getQuote(quoteId), HttpStatus.OK);
    }

    @PostMapping("/folders/{folderId}")
    public ResponseEntity<?> createQuote(
            @PathVariable(name = "folderId") Long folderId,
            @ModelAttribute @Valid QuoteDto.QuoteRequest quoteRequestDto,
            @CurrentUser Principal currentUser) {
        log.info("[quotedebug] quoteRequestDto : {} currentUser : {}", quoteRequestDto, currentUser.getName());
        return new ResponseEntity<>(quoteService.createQuote(folderId, quoteRequestDto, currentUser), HttpStatus.OK);
    }

    @PostMapping("/{quoteId}")
    public ResponseEntity<?> updateQuote(
            @PathVariable(name = "quoteId") Long quoteId,
            @ModelAttribute QuoteDto.QuoteRequest quoteRequestDto
    ) {
        if (quoteRequestDto.getQuoteText() == null) {
            throw new RuntimeException("need paramter!!!");
        }
        return new ResponseEntity<>(quoteService.updateQuote(quoteId, quoteRequestDto), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteQuotes(
            @RequestParam(value = "quoteIds") List<Long> quoteIds) {
        Map<String, Object> result = new HashMap<>();

        //todo : 사용자가 명언을 삭제하면 다른 임시 폴더로 옮겨서 review할 수 있는 페이지에서 confirm하는 방식으로 하면 좋을 듯함.
        // 이렇게 하면 Quote_History에서 남아 있는 데이터는 계속 유지될 수 있음

        int status = quoteService.deleteQuotes(quoteIds);
        result.put("succeed", status == quoteIds.size());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/move/{folderId}")
    public ResponseEntity<?> moveQuotes(@RequestParam(value = "quoteIds") List<Long> quoteIds,
                                        @PathVariable(name = "folderId") Long folderId) {
        Map<String, Object> result = new HashMap<>();
        result.put("succeed", quoteService.moveQuotes(quoteIds, folderId));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/checkQuoteExists")
    public ResponseEntity<?> checkQuoteExists(@RequestParam(value = "quoteText") String quoteText) {
        if (quoteText.isEmpty()) {
            throw new ApiException(QuoteExceptionCode.REQUEST_INVALID);
        }
        return new ResponseEntity<>(quoteService.doesQuoteExists(quoteText), HttpStatus.OK);
    }

    //likes
    @PostMapping("/{quoteId}/likes")
    public ResponseEntity<?> registerQuoteLike(
            @PathVariable(value = "quoteId") Long quoteId, @CurrentUser Principal currentUser) {
        log.debug("[quotedebug] quoteId : {} currentUser : {}", quoteId, currentUser.getName());
        return new ResponseEntity<>(quoteLikeService.registerAndGetQuoteLikeInfo(quoteId, currentUser.getName()), HttpStatus.OK);
    }

    @GetMapping("/{quoteId}/likes")
    public ResponseEntity<?> getQuoteLike(
            @PathVariable(value = "quoteId") Long quoteId, @CurrentUser Principal currentUser) {
        log.debug("[quotedebug] quoteId : {} currentUser : {}", quoteId, currentUser.getName());
        return new ResponseEntity<>(quoteLikeService.getRegisteredQuoteLikeInfo(quoteId, currentUser.getName()), HttpStatus.OK);
    }

    @DeleteMapping("/{quoteId}/likes")
    public ResponseEntity<?> unregisterQuoteLike(
            @PathVariable(value = "quoteId") Long quoteId, @CurrentUser Principal currentUser) {
        log.debug("[quotedebug] quoteId : {} currentUser : {}", quoteId, currentUser.getName());
        return new ResponseEntity<>(quoteLikeService.unregisterAndGetQuoteLikeInfo(quoteId, currentUser.getName()), HttpStatus.OK);
    }

    @GetMapping("/today")
    public ResponseEntity<?> getTodayQuotes(
            @RequestParam(value = "pageIndex", defaultValue = AppConstants.DEFAULT_PAGE_INDEX) Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer pageSize) {
        return new ResponseEntity<>(quoteService.getTodayQuotes(pageIndex, pageSize), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/random")
    public ResponseEntity<?> getRandomQuote() {
        return new ResponseEntity<>(modelMapper.map(quoteService.getRandomQuote(), QuoteDto.QuoteResponse.class), HttpStatus.OK);
    }
}
