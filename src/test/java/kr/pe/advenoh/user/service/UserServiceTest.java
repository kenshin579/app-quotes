package kr.pe.advenoh.user.service;

import kr.pe.advenoh.admin.folder.domain.FolderDto;
import kr.pe.advenoh.admin.folder.service.FolderService;
import kr.pe.advenoh.common.constants.AppConstants;
import kr.pe.advenoh.common.exception.QuoteExceptionCode;
import kr.pe.advenoh.user.domain.AccountDto;
import kr.pe.advenoh.user.domain.Role;
import kr.pe.advenoh.user.domain.RoleRepository;
import kr.pe.advenoh.user.domain.RoleType;
import kr.pe.advenoh.user.domain.User;
import kr.pe.advenoh.user.domain.UserRepository;
import kr.pe.advenoh.util.MockitoTestSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
class UserServiceTest extends MockitoTestSupport {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Spy
    private ModelMapper modelMapper;

    @Mock
    private FolderService folderService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void registerNewUserAccount_저장() {
        //given
        AccountDto.SignUpRequestDto signUpRequestDto = AccountDto.SignUpRequestDto.builder()
                .username(username)
                .email(email)
                .name(name)
                .password(password)
                .build();


        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        when(roleRepository.findByRoleType(RoleType.ROLE_USER)).thenReturn(Optional.of(new Role()));
        when(folderService.createFolder(AppConstants.DEFAULT_FOLDER, signUpRequestDto.getUsername())).thenReturn(FolderDto.FolderResponse.builder().build());
        when(userRepository.save(any())).thenReturn(any());

        //when
        userService.registerNewUserAccount(signUpRequestDto);

        //then
        verify(userRepository).save(userArgumentCaptor.capture());
        assertThat(userArgumentCaptor.getValue().getUsername()).isEqualTo(signUpRequestDto.getUsername());
        assertThat(userArgumentCaptor.getValue().isEnabled()).isTrue();
    }

    @Test
    void registerNewUserAccount_username이_존재하는_경우() {
        //given
        AccountDto.SignUpRequestDto signUpRequestDto = AccountDto.SignUpRequestDto.builder()
                .username(username)
                .build();

        when(userRepository.existsByUsername(signUpRequestDto.getUsername())).thenReturn(true);

        //when
        //then
        assertThatThrownBy(() -> userService.registerNewUserAccount(signUpRequestDto))
                .hasMessageContaining(QuoteExceptionCode.ACCOUNT_USERNAME_IS_ALREADY_EXIST.getMessage());

    }

    @Test
    void registerNewUserAccount_email이_존재하는_경우() {
        //given
        AccountDto.SignUpRequestDto signUpRequestDto = AccountDto.SignUpRequestDto.builder()
                .email(email)
                .build();

        when(userRepository.existsByEmail(signUpRequestDto.getEmail())).thenReturn(true);

        //when
        //then
        assertThatThrownBy(() -> userService.registerNewUserAccount(signUpRequestDto))
                .hasMessageContaining(QuoteExceptionCode.ACCOUNT_EMAIL_IS_ALREADY_EXIST.getMessage());
    }


}