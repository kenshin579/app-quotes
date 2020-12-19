package kr.pe.advenoh.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.pe.advenoh.common.exception.ErrorCode;
import kr.pe.advenoh.user.domain.AccountDto;
import kr.pe.advenoh.util.SpringMockMvcTestSupport;
import kr.pe.advenoh.util.TestUtils;
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
                .content(new ObjectMapper().writeValueAsBytes(AccountDto.SignUpRequestDto.builder()
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
                .content(new ObjectMapper().writeValueAsBytes(AccountDto.LoginRequestDto.builder()
                        .password(password)
                        .username(username)
                        .build())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());

    }

    @Test
    @Transactional
    void registerUser_email형식_아닌_경우_Exception이_발생한다() throws Exception {
        String username = TestUtils.generateRandomString(5);
        log.debug("username : {}", username);

        //user 생성
        this.mockMvc.perform(post(BASE_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsBytes(AccountDto.SignUpRequestDto.builder()
                        .name(name)
                        .password(password)
                        .email("1234.com")
                        .username(username)
                        .build())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCode.REQUEST_INVALID.getCode())));

    }

    @Test
    @Transactional
    void registerUser_request값이_없이_요청하는_경우_Exception이_발생한다() throws Exception {
        String username = TestUtils.generateRandomString(5);
        log.debug("username : {}", username);

        //user 생성
        this.mockMvc.perform(post(BASE_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsBytes(AccountDto.SignUpRequestDto.builder()
                        .name(name)
                        .password(password)
                        .build())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCode.REQUEST_INVALID.getCode())));

    }
}