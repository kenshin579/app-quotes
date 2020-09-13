package kr.pe.advenoh.quote.controller;

import com.jayway.jsonpath.JsonPath;
import kr.pe.advenoh.quote.exception.ApiException;
import kr.pe.advenoh.quote.exception.QuoteExceptionCode;
import kr.pe.advenoh.quote.model.entity.QuoteTagMapping;
import kr.pe.advenoh.quote.model.entity.Role;
import kr.pe.advenoh.quote.model.entity.User;
import kr.pe.advenoh.quote.model.enums.RoleType;
import kr.pe.advenoh.quote.model.enums.YN;
import kr.pe.advenoh.quote.repository.RoleRepository;
import kr.pe.advenoh.quote.repository.quote.QuoteTagMappingRepository;
import kr.pe.advenoh.quote.spring.InitialDataLoader;
import kr.pe.advenoh.quote.util.SpringMockMvcTestSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

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
@ActiveProfiles("jdbc")
class QuoteControllerTest extends SpringMockMvcTestSupport {
    private final String BASE_PATH = "/api/quotes";
    private List<String> tags;
    private User user;
    private Long folderId;
    private Long quoteId;

    @Autowired
    private InitialDataLoader initialDataLoader;

    @Autowired
    private QuoteTagMappingRepository quoteTagMappingRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        Role role = roleRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(() ->
                new ApiException(QuoteExceptionCode.ACCOUNT_ROLE_NOT_FOUND, RoleType.ROLE_USER.name()));
        user = initialDataLoader.createUserIfNotFound(username, email, name, password, Arrays.asList(role));
        folderId = jdbcTemplate.queryForObject("SELECT folder_id FROM app_quotes.folders LIMIT 1", Long.class);
        quoteId = jdbcTemplate.queryForObject("SELECT quote_id FROM app_quotes.quotes LIMIT 1", Long.class);
        log.debug("folderId : {} quoteId: {} user: {}", folderId, quoteId, user);
    }

    @Test
    @WithMockUser(username = username, authorities = {ROLE_USER})
    @Transactional
    void createQuote_getQuote_updateQuote_deleteQuote_새로운_tags로만_생성함() throws Exception {
        //명언 생성
        tags = this.getRandomTags("first", 3);
        log.info("tags : {}", tags);

        String quoteText = "quote text1";
        String authorName = "test author";
        MvcResult mvcResult = this.mockMvc.perform(post(BASE_PATH + "/folders/{folderId}", folderId)
                .param("quoteText", quoteText)
                .param("authorName", authorName)
                .param("useYn", YN.Y.name())
                .param("tags", String.join(",", tags)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quoteText", is(quoteText)))
                .andExpect(jsonPath("$.authorName", is(authorName)))
                .andReturn();

        Integer quoteId = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.quoteId");
        log.info("quoteId : {}", quoteId);

        //명언 조회
        this.mockMvc.perform(get(BASE_PATH + "/{quoteId}", quoteId))
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
        log.info("new tags : {}", tags);

        this.mockMvc.perform(post(BASE_PATH + "/{quoteId}", quoteId)
                .param("quoteText", quoteText)
                .param("authorName", authorName)
                .param("useYn", YN.N.name())
                .param("tags", String.join(",", tags)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quoteId", is(quoteId)))
                .andExpect(jsonPath("$.quoteText", is(quoteText)))
                .andExpect(jsonPath("$.authorName", is(authorName)));

        //명언 조회
        this.mockMvc.perform(get(BASE_PATH + "/{quoteId}", quoteId))
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
        this.mockMvc.perform(delete(BASE_PATH)
                .param("quoteIds", quoteId.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.succeed", is(true)));
    }

    //todo : 여기 작업하기
    @Test
    @WithMockUser(username = username, authorities = {ROLE_USER})
    @Transactional
    void createQuote_request_값이_없는_경우() throws Exception {
        this.mockMvc.perform(post(BASE_PATH + "/folders/{folderId}", folderId)
                .param("useYn", YN.Y.name()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = username, authorities = {ROLE_USER})
    void getQuotes() throws Exception {
        this.mockMvc.perform(get(BASE_PATH + "/folders/{folderId}", folderId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @WithMockUser(username = username, authorities = {ROLE_USER})
    void getQuote() throws Exception {
        this.mockMvc.perform(get(BASE_PATH + "/{quoteId}", quoteId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quoteId", is(quoteId.intValue())));
    }

    @Test
    @WithMockUser(username = username, authorities = {ROLE_USER})
    void getQuote_ApiException_발생시_response_포멧_확인() throws Exception {
        this.mockMvc.perform(get(BASE_PATH + "/{quoteId}", Integer.MAX_VALUE))
                .andDo(print())
                .andExpect(status().isServiceUnavailable())
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