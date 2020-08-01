package kr.pe.advenoh.quote.controller;

import kr.pe.advenoh.quote.exception.ApiException;
import kr.pe.advenoh.quote.exception.QuoteExceptionCode;
import kr.pe.advenoh.quote.model.dto.QuoteRequestDto;
import kr.pe.advenoh.quote.service.QuoteLikeService;
import kr.pe.advenoh.quote.service.QuoteService;
import kr.pe.advenoh.quote.spring.security.CurrentUser;
import kr.pe.advenoh.quote.util.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private QuoteLikeService quoteLikeService;

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
            @ModelAttribute QuoteRequestDto quoteRequestDto,
            @CurrentUser Principal currentUser) {
        log.info("[quotedebug] quoteRequestDto : {} currentUser : {}", quoteRequestDto, currentUser.getName());
        //todo : 이건 @Valid 어노테이션을 변경작업하도록 함
        if (quoteRequestDto.getQuoteText() == null
                || quoteRequestDto.getUseYn() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, QuoteExceptionCode.REQUEST_INVALID);
        }
        quoteRequestDto.setFolderId(folderId);
        return new ResponseEntity<>(quoteService.createQuote(quoteRequestDto, currentUser), HttpStatus.OK);
    }

    @PostMapping("/{quoteId}")
    public ResponseEntity<?> updateQuote(
            @PathVariable(name = "quoteId") Long quoteId,
            @ModelAttribute QuoteRequestDto quoteRequestDto
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

    //likes
    @PostMapping("/{quoteId}/likes")
    public ResponseEntity<?> registerVodLike(
            @PathVariable(value = "quoteId") Long quoteId, @CurrentUser Principal currentUser) {
        log.debug("[quotedebug] quoteId : {} currentUser : {}", quoteId, currentUser.getName());
        return new ResponseEntity<>(quoteLikeService.registerAndGetQuoteLikeInfo(quoteId, currentUser.getName()), HttpStatus.OK);
    }

    @GetMapping("/{quoteId}/likes")
    public ResponseEntity<?> getVodLike(
            @PathVariable(value = "quoteId") Long quoteId, @CurrentUser Principal currentUser) {
        log.debug("[quotedebug] quoteId : {} currentUser : {}", quoteId, currentUser.getName());
        return new ResponseEntity<>(quoteLikeService.getRegisteredQuoteLikeInfo(quoteId, currentUser.getName()), HttpStatus.OK);
    }

    @DeleteMapping("/{quoteId}/likes")
    public ResponseEntity<?> unregisterVodLike(
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

    @GetMapping("/random")
    public ResponseEntity<?> getRandomQuote() {
        return new ResponseEntity<>(quoteService.getRandomQuote(), HttpStatus.OK);
    }
}
