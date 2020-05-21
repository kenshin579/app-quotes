package kr.pe.advenoh.quote.controller;

import kr.pe.advenoh.quote.service.QuoteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(QuoteController.class)
public class QuoteControllerTest {
    private final String BASE_PATH = "/api/quotes";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private QuoteService quoteService;

    @Test
    public void createQuote() throws Exception {
        mvc.perform(post(BASE_PATH + "/job")
                .param("jobName", "job1")
                .param("groupName", "testGroup"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)));
    }
}