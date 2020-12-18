package kr.pe.advenoh.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.pe.advenoh.common.exception.ApiException;
import kr.pe.advenoh.common.exception.QuoteExceptionCode;
import kr.pe.advenoh.spring.InitialDataLoader;
import kr.pe.advenoh.user.domain.AccountDto;
import kr.pe.advenoh.user.domain.Role;
import kr.pe.advenoh.user.domain.RoleRepository;
import kr.pe.advenoh.user.domain.RoleType;
import kr.pe.advenoh.user.domain.User;
import kr.pe.advenoh.util.SpringMockMvcTestSupport;
import kr.pe.advenoh.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;
import java.util.Arrays;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class UserControllerTest extends SpringMockMvcTestSupport {
    private final String BASE_PATH = "/api/user";
    private User user;

    @Autowired
    private InitialDataLoader initialDataLoader;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        Role role = roleRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(() ->
                new ApiException(QuoteExceptionCode.ACCOUNT_ROLE_NOT_FOUND, RoleType.ROLE_USER.name()));
        AccountDto.SignUpRequestDto requestNewUser = AccountDto.SignUpRequestDto.builder()
                .username(username)
                .email(email)
                .name(name)
                .password(password)
                .build();

        user = initialDataLoader.createUserIfNotFound(requestNewUser, Arrays.asList(role));
    }

    @Disabled
    @Test
    @WithMockUser(username = username, authorities = {ROLE_USER})
    void getCurrentUser() throws Exception {
        this.mockMvc.perform(get(BASE_PATH + "/me"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void deleteUser() throws Exception {
        String username = TestUtils.generateRandomString(5);
        log.debug("username : {}", username);

        //user 생성
        this.mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsBytes(AccountDto.SignUpRequestDto.builder()
                        .name(name)
                        .password(password)
                        .email(username + "@gmail.com")
                        .username(username)
                        .build())))
                .andDo(print())
                .andExpect(status().isCreated());

        //user 삭제
        this.mockMvc.perform(delete("/api/user/{username}", username)
                .with(user(username)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}