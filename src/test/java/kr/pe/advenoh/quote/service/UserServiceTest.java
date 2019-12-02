package kr.pe.advenoh.quote.service;

import kr.pe.advenoh.quote.model.Role;
import kr.pe.advenoh.quote.model.User;
import kr.pe.advenoh.quote.model.enums.RoleType;
import kr.pe.advenoh.quote.repository.RoleRepository;
import kr.pe.advenoh.quote.repository.UserRepository;
import kr.pe.advenoh.quote.util.DefaultMockitoConfig;
import kr.pe.advenoh.quote.web.dto.request.SignUpRequest;
import kr.pe.advenoh.quote.web.exception.QuoteExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
public class UserServiceTest extends DefaultMockitoConfig {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void registerNewUserAccount_저장() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername(username);
        signUpRequest.setEmail(email);
        signUpRequest.setName(name);
        signUpRequest.setPassword(password);

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn(password);
        when(roleRepository.findByRoleType(RoleType.ROLE_USER)).thenReturn(new Role());
        when(userRepository.save(any())).thenReturn(any());

        userService.registerNewUserAccount(signUpRequest);

        verify(userRepository).save(userArgumentCaptor.capture());
        assertThat(userArgumentCaptor.getValue().getUsername()).isEqualTo(signUpRequest.getUsername());
    }

    @Test
    public void registerNewUserAccount_username이_존재하는_경우() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername(username);

        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(true);

        assertThatThrownBy(() -> userService.registerNewUserAccount(signUpRequest))
                .hasMessageContaining(QuoteExceptionCode.ACCOUNT_USERNAME_IS_ALREADY_EXIST.getMessage());

    }

    @Test
    public void registerNewUserAccount_email이_존재하는_경우() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(email);

        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.registerNewUserAccount(signUpRequest))
                .hasMessageContaining(QuoteExceptionCode.ACCOUNT_EMAIL_IS_ALREADY_EXIST.getMessage());
    }


}