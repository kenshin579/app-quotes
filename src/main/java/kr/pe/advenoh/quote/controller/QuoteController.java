package kr.pe.advenoh.quote.controller;

import kr.pe.advenoh.quote.model.dto.QuoteRequestDto;
import kr.pe.advenoh.quote.service.QuoteLikeService;
import kr.pe.advenoh.quote.service.QuoteService;
import kr.pe.advenoh.quote.spring.security.CurrentUser;
import kr.pe.advenoh.quote.util.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Object getQuotes(
            @PathVariable(name = "folderId") Long folderId,
            @RequestParam(value = "pageIndex", defaultValue = AppConstants.DEFAULT_PAGE_INDEX) Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer pageSize) {
        return quoteService.getQuotes(folderId, pageIndex, pageSize);
    }

    //todo : 좋아요 정보를 같이 내려주는 게 좋아보임
    @GetMapping("/{quoteId}")
    public Object getQuote(@PathVariable(name = "quoteId") Long quoteId) {
        return quoteService.getQuote(quoteId);
    }

    @PostMapping("/folders/{folderId}")
    public Object createQuote(
            @PathVariable(name = "folderId") Long folderId,
            @ModelAttribute QuoteRequestDto quoteRequestDto,
            @CurrentUser Principal currentUser) {
        log.info("[quotedebug] quoteRequestDto : {} currentUser : {}", quoteRequestDto, currentUser.getName());
        if (quoteRequestDto.getQuoteText() == null) {
            throw new RuntimeException("need paramter!!!");
        }
        quoteRequestDto.setFolderId(folderId);
        return quoteService.createQuote(quoteRequestDto, currentUser);
    }

    @PostMapping("/{quoteId}")
    public Object updateQuote(
            @PathVariable(name = "quoteId") Long quoteId,
            @ModelAttribute QuoteRequestDto quoteRequestDto
    ) {
        if (quoteRequestDto.getQuoteText() == null) {
            throw new RuntimeException("need paramter!!!");
        }
        return quoteService.updateQuote(quoteId, quoteRequestDto);
    }

    @DeleteMapping
    public Object deleteQuotes(
            @RequestParam(value = "quoteIds") List<Long> quoteIds) {
        Map<String, Object> result = new HashMap<>();

        //todo : 사용자가 명언을 삭제하면 다른 임시 폴더로 옮겨서 review할 수 있는 페이지에서 confirm하는 방식으로 하면 좋을 듯함.
        // 이렇게 하면 Quote_History에서 남아 있는 데이터는 계속 유지될 수 있음

        int status = quoteService.deleteQuotes(quoteIds);
        result.put("succeed", status == quoteIds.size());
        return result;
    }


    @PutMapping("/move/{folderId}")
    public Object moveQuotes(@RequestParam(value = "quoteIds") List<Long> quoteIds,
                             @PathVariable(name = "folderId") Long folderId) {
        Map<String, Object> result = new HashMap<>();
        result.put("succeed", quoteService.moveQuotes(quoteIds, folderId));
        return result;
    }

    //likes
    @PostMapping("/{quoteId}/likes")
    public Object registerVodLike(
            @PathVariable(value = "quoteId") Long quoteId, @CurrentUser Principal currentUser) {
        log.debug("[quotedebug] quoteId : {} currentUser : {}", quoteId, currentUser.getName());
        return quoteLikeService.registerAndGetQuoteLikeInfo(quoteId, currentUser.getName());
    }

    @GetMapping("/{quoteId}/likes")
    public Object getVodLike(
            @PathVariable(value = "quoteId") Long quoteId, @CurrentUser Principal currentUser) {
        log.debug("[quotedebug] quoteId : {} currentUser : {}", quoteId, currentUser.getName());
        return quoteLikeService.getRegisteredQuoteLikeInfo(quoteId, currentUser.getName());
    }

    @DeleteMapping("/{quoteId}/likes")
    public Object unregisterVodLike(
            @PathVariable(value = "quoteId") Long quoteId, @CurrentUser Principal currentUser) {
        log.debug("[quotedebug] quoteId : {} currentUser : {}", quoteId, currentUser.getName());
        return quoteLikeService.unregisterAndGetQuoteLikeInfo(quoteId, currentUser.getName());
    }

    @GetMapping("/today")
    public Object getTodayQuotes(
            @RequestParam(value = "pageIndex", defaultValue = AppConstants.DEFAULT_PAGE_INDEX) Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer pageSize) {
        return quoteService.getTodayQuotes(pageIndex, pageSize);
    }

    @GetMapping("/random")
    public Object getRandomQuote() {
        return quoteService.getRandomQuote();
    }
}
