package kr.pe.advenoh.quote.controller;

import kr.pe.advenoh.quote.service.QuoteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
//@WebMvcTest(QuoteController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class QuoteControllerTest {
    private final String BASE_PATH = "/api/quotes";

    @Autowired
    private MockMvc mvc;

//    @MockBean
//    private QuoteService quoteService;

    @Test
    @Ignore
    public void createQuote() throws Exception {
        this.mvc.perform(post(BASE_PATH + "/job")
                .param("jobName", "job1")
                .param("groupName", "testGroup"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    @WithMockUser(username = "kenshin579", authorities = {"USER"})
    public void getQuotes() throws Exception {
        this.mvc.perform(get(BASE_PATH + "/folders/{folderId}", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "kenshin579", authorities = {"USER"})
    public void getQuote() throws Exception {
        this.mvc.perform(get(BASE_PATH + "/{quoteId}", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }
}