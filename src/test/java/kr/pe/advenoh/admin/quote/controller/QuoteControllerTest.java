package kr.pe.advenoh.admin.quote.controller;

import com.jayway.jsonpath.JsonPath;
import kr.pe.advenoh.admin.folder.service.FolderService;
import kr.pe.advenoh.admin.quote.domain.QuoteTagMapping;
import kr.pe.advenoh.admin.quote.domain.QuoteTagMappingRepository;
import kr.pe.advenoh.admin.quote.domain.QuoteDto;
import kr.pe.advenoh.admin.quote.domain.enums.YN;
import kr.pe.advenoh.common.exception.QuoteExceptionCode;
import kr.pe.advenoh.spring.InitialDataLoader;
import kr.pe.advenoh.user.domain.Privilege;
import kr.pe.advenoh.user.domain.PrivilegeType;
import kr.pe.advenoh.user.domain.Role;
import kr.pe.advenoh.user.domain.RoleType;
import kr.pe.advenoh.user.domain.User;
import kr.pe.advenoh.util.SpringMockMvcTestSupport;
import kr.pe.advenoh.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

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
class QuoteControllerTest extends SpringMockMvcTestSupport {
    private final String BASE_PATH = "/api/quotes";
    private List<String> tags;
    private User user;
    private String folderName;
    private Long folderId;

    @Autowired
    private InitialDataLoader initialDataLoader;

    @Autowired
    private FolderService folderService;

    @Autowired
    private QuoteTagMappingRepository quoteTagMappingRepository;

    @BeforeEach
    void setUp() {
        //todo : pre data를 어떻게 로딩할지는 더 고민해보기
        Privilege readPrivilege = initialDataLoader.createPrivilegeIfNotFound(PrivilegeType.READ_PRIVILEGE);
        Privilege passwordPrivilege = initialDataLoader.createPrivilegeIfNotFound(PrivilegeType.CHANGE_PASSWORD_PRIVILEGE);

        List<Privilege> userPrivileges = Arrays.asList(readPrivilege, passwordPrivilege);
        Role role = initialDataLoader.createRoleIfNotFound(RoleType.ROLE_USER, userPrivileges);

        user = initialDataLoader.createUserIfNotFound(username, email, name, password, Arrays.asList(role));
        folderName = TestUtils.generateRandomString(3);
        folderId = folderService.createFolder(folderName, username).getFolderId();

        log.debug("folderId : {} user: {}", folderId, user);
    }

    @Test
    @Transactional
    @WithMockUser(username = username, authorities = {ROLE_USER})
    void createQuote_getQuote() throws Exception {
        //명언 생성
        QuoteDto.QuoteRequest quoteRequest = this.buildQuoteRequest();

        MvcResult mvcResult = requestCreateQuote(folderId, quoteRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quoteText", is(quoteText)))
                .andExpect(jsonPath("$.authorName", is(authorName)))
                .andReturn();

        Integer quoteId = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.quoteId");
        log.debug("quoteId : {}", quoteId);

        //명언 조회
        ResultActions resultActions = requestGetQuoteById(quoteId);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quoteId", is(quoteId)))
                .andExpect(jsonPath("$.quoteText", is(quoteText)))
                .andExpect(jsonPath("$.useYn", is(YN.Y.name())))
                .andExpect(jsonPath("$.authorName", is(authorName)))
                .andExpect(jsonPath("$.tags", is(quoteRequest.getTags())));
    }

    @Test
    @Transactional
    @WithMockUser(username = username, authorities = {ROLE_USER})
    void updateQuote() throws Exception {
        //명언 생성
        QuoteDto.QuoteRequest quoteRequest = this.buildQuoteRequest();

        MvcResult mvcResult = requestCreateQuote(folderId, quoteRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quoteText", is(quoteText)))
                .andExpect(jsonPath("$.authorName", is(authorName)))
                .andReturn();

        Integer quoteId = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.quoteId");
        log.debug("quoteId : {}", quoteId);

        //명언 업데이트
        String newQuoteText = "new quote text";
        String newAuthorName = "new author";
        tags.addAll(this.getRandomTags("second", 1));
        log.debug("new tags : {}", tags);

        this.mockMvc.perform(post(BASE_PATH + "/{quoteId}", quoteId)
                .param("quoteText", newQuoteText)
                .param("authorName", newAuthorName)
                .param("useYn", YN.N.name())
                .param("tags", String.join(",", tags)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quoteId", is(quoteId)))
                .andExpect(jsonPath("$.quoteText", is(newQuoteText)))
                .andExpect(jsonPath("$.authorName", is(newAuthorName)));

        //명언 조회
        ResultActions resultActions = requestGetQuoteById(quoteId);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quoteId", is(quoteId)))
                .andExpect(jsonPath("$.quoteText", is(newQuoteText)))
                .andExpect(jsonPath("$.useYn", is(YN.N.name())))
                .andExpect(jsonPath("$.authorName", is(newAuthorName)))
                .andExpect(jsonPath("$.tags", is(tags)));
    }

    @Test
    @Transactional
    @WithMockUser(username = username, authorities = {ROLE_USER})
    void deleteQuote() throws Exception {
        //명언 생성
        QuoteDto.QuoteRequest quoteRequest = this.buildQuoteRequest();

        MvcResult mvcResult = requestCreateQuote(folderId, quoteRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quoteText", is(quoteText)))
                .andExpect(jsonPath("$.authorName", is(authorName)))
                .andReturn();

        Integer quoteId = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.quoteId");
        log.debug("quoteId : {}", quoteId);

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

    @Test
    @Transactional
    @WithMockUser(username = username, authorities = {ROLE_USER})
    void createQuote_exception_request_값이_없는_경우() throws Exception {
        this.mockMvc.perform(post(BASE_PATH + "/folders/{folderId}", folderId)
                .param("useYn", YN.Y.name()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(QuoteExceptionCode.REQUEST_INVALID.getMessage())));
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
    void getQuote_ApiException_발생시_response_포멧_확인() throws Exception {
        requestGetQuoteById(Integer.MAX_VALUE)
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message", is(QuoteExceptionCode.QUOTE_NOT_FOUND.getMessage())))
                .andExpect(jsonPath("$.code", is(QuoteExceptionCode.QUOTE_NOT_FOUND.getCode())));
    }

    @Test
    public void checkQuoteExists() throws Exception {
        this.mockMvc.perform(get(BASE_PATH + "/checkQuoteExists")
                .param("quoteText", quoteText))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isBoolean());
    }

    @Test
    @Disabled
    public void getRandomQuote() throws Exception {
        this.mockMvc.perform(get(BASE_PATH + "/random"))
                .andDo(print())
                .andExpect(status().isOk());
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

        return sb.toString();
    }

    private ResultActions requestCreateQuote(Long folderId, QuoteDto.QuoteRequest quoteRequest) throws Exception {
        return this.mockMvc.perform(post(BASE_PATH + "/folders/{folderId}", folderId)
                .param("quoteText", quoteRequest.getQuoteText())
                .param("authorName", quoteRequest.getAuthorName())
                .param("useYn", quoteRequest.getUseYn().name())
                .param("tags", String.join(",", quoteRequest.getTags())))
                .andDo(print());
    }

    private QuoteDto.QuoteRequest buildQuoteRequest() {
        return QuoteDto.QuoteRequest.builder()
                .quoteText(quoteText)
                .authorName(authorName)
                .tags(this.getRandomTags("first", 3))
                .useYn(YN.Y)
                .build();
    }

    private ResultActions requestGetQuoteById(Integer quoteId) throws Exception {
        return this.mockMvc.perform(get(BASE_PATH + "/{quoteId}", quoteId))
                .andDo(print());
    }
}