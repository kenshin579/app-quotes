package kr.pe.advenoh.quote.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.pe.advenoh.quote.model.dto.LoginRequestDto;
import kr.pe.advenoh.quote.model.dto.SignUpRequestDto;
import kr.pe.advenoh.quote.util.SpringMockMvcTestSupport;
import kr.pe.advenoh.quote.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class AuthControllerTest extends SpringMockMvcTestSupport {
    private final String BASE_URL = "/api/auth";

    @Test
    @Transactional
    void registerUser_authenticateUser() throws Exception {
        String username = TestUtils.generateRandomString(5);
        log.debug("username : {}", username);

        //user 생성
        this.mockMvc.perform(post(BASE_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsBytes(SignUpRequestDto.builder()
                        .name(name)
                        .password(password)
                        .email(username + "@gmail.com")
                        .username(username)
                        .build())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)));

        //login
        this.mockMvc.perform(post(BASE_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsBytes(LoginRequestDto.builder()
                        .password(password)
                        .username(username)
                        .build())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());

    }
}