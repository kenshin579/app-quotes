package kr.pe.advenoh.quote.controller;

import com.jayway.jsonpath.JsonPath;
import kr.pe.advenoh.quote.exception.QuoteExceptionCode;
import kr.pe.advenoh.quote.model.entity.QuoteTagMapping;
import kr.pe.advenoh.quote.model.enums.YN;
import kr.pe.advenoh.quote.repository.quote.QuoteTagMappingRepository;
import kr.pe.advenoh.quote.util.SpringBootTestSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.RollbackException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc
class QuoteControllerBootTest extends SpringBootTestSupport {
    private final String BASE_PATH = "/api/quotes";

    @Autowired
    private MockMvc mvc;

//    @MockBean
//    private QuoteService quoteService;

    @Autowired
    private QuoteTagMappingRepository quoteTagMappingRepository;

    private List<String> tags;

//    @BeforeEach
//    void setUp() {
//	    createUserIfNotFound( Arrays.asList());
//    }

    @Test
    @WithMockUser(username = username, authorities = { ROLE_USER })
    @Transactional
    void createQuote_getQuote_updateQuote_deleteQuote_새로운_tags로만_생성함() throws Exception {
        //명언 생성
        Long folderId = 1L;
        tags = this.getRandomTags("first", 3);

        log.info("[quotedebug] tags : {}", tags);

        String quoteText = "quote text1";
        String authorName = "test author";
        MvcResult mvcResult = this.mvc.perform(post(BASE_PATH + "/folders/{folderId}", folderId)
                .param("quoteText", quoteText)
                .param("authorName", authorName)
                .param("useYn", YN.Y.name())
                .param("tags", String.join(",", tags)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Integer quoteId = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.quoteId");
        log.info("[quotedebug] quoteId : {}", quoteId);

        //명언 조회
        this.mvc.perform(get(BASE_PATH + "/{quoteId}", quoteId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quoteId", is(quoteId)))
                .andExpect(jsonPath("$.quoteText", is(quoteText)))
                .andExpect(jsonPath("$.useYn", is(YN.Y.name())))
                .andExpect(jsonPath("$.authorName", is(authorName)))
                .andExpect(jsonPath("$.tags", is(tags)));

        //명언 업데이트
        quoteText = "new quote text";
        authorName = "new author";
        tags.addAll(this.getRandomTags("second", 1));
        log.info("[quotedebug] new tags : {}", tags);

        this.mvc.perform(post(BASE_PATH + "/{quoteId}", quoteId)
                .param("quoteText", quoteText)
                .param("authorName", authorName)
                .param("useYn", YN.N.name())
                .param("tags", String.join(",", tags)))
                .andDo(print())
                .andExpect(status().isOk());

        //명언 조회
        this.mvc.perform(get(BASE_PATH + "/{quoteId}", quoteId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quoteId", is(quoteId)))
                .andExpect(jsonPath("$.quoteText", is(quoteText)))
                .andExpect(jsonPath("$.useYn", is(YN.N.name())))
                .andExpect(jsonPath("$.authorName", is(authorName)))
                .andExpect(jsonPath("$.tags", is(tags)));

        //mapping도 잘 되어 있는지 확인
        List<QuoteTagMapping> quoteTagMappings = quoteTagMappingRepository.findAllByQuoteIds(Arrays.asList(quoteId.longValue()));
        assertThat(quoteTagMappings.size()).isEqualTo(tags.size());

        //명언 삭제
        this.mvc.perform(delete(BASE_PATH)
                .param("quoteIds", quoteId.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.succeed", is(true)));
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"USER"})
    void getQuotes() throws Exception {
        this.mvc.perform(get(BASE_PATH + "/folders/{folderId}", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"USER"})
    void getQuote() throws Exception {
        this.mvc.perform(get(BASE_PATH + "/{quoteId}", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"USER"})
    void getQuote_ApiException_발생시_response_포멧_확인() throws Exception {
        this.mvc.perform(get(BASE_PATH + "/{quoteId}", Integer.MAX_VALUE))
                .andDo(print())
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status", is("SERVICE_UNAVAILABLE")))
                .andExpect(jsonPath("$.message", is(QuoteExceptionCode.QUOTE_NOT_FOUND.getMessage())))
                .andExpect(jsonPath("$.code", is(QuoteExceptionCode.QUOTE_NOT_FOUND.getCode())));
    }

    private List<String> getRandomTags(String prefix, int max) {
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            tags.add(prefix + "_" + this.getRandomStr(10));
        }
        return tags;
    }

    private String getRandomStr(int length) {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRST".toCharArray();

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String randomStr = sb.toString();

        return randomStr;
    }
}