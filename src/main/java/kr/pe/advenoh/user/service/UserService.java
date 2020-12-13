package kr.pe.advenoh.user.service;

import kr.pe.advenoh.admin.folder.domain.dto.FolderDto;
import kr.pe.advenoh.admin.folder.service.FolderService;
import kr.pe.advenoh.common.constants.AppConstants;
import kr.pe.advenoh.common.exception.ApiException;
import kr.pe.advenoh.common.exception.QuoteExceptionCode;
import kr.pe.advenoh.user.domain.RoleRepository;
import kr.pe.advenoh.user.domain.RoleType;
import kr.pe.advenoh.user.domain.User;
import kr.pe.advenoh.user.domain.UserRepository;
import kr.pe.advenoh.user.domain.dto.SignUpRequestDto;
import kr.pe.advenoh.user.domain.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final FolderService folderService;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public User registerNewUserAccount(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByUsername(signUpRequestDto.getUsername())) {
            throw new ApiException(QuoteExceptionCode.ACCOUNT_USERNAME_IS_ALREADY_EXIST);
        }

        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new ApiException(QuoteExceptionCode.ACCOUNT_EMAIL_IS_ALREADY_EXIST);
        }

        //새로운 사용자 생성
        User user = modelMapper.map(signUpRequestDto, User.class);
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findByRoleType(RoleType.ROLE_USER)
                .orElseThrow(() -> new ApiException(
                        QuoteExceptionCode.ACCOUNT_ROLE_NOT_FOUND,
                        RoleType.ROLE_USER.name()
                ))));

        User save = userRepository.save(user);
        folderService.createFolder(AppConstants.DEFAULT_FOLDER, user.getUsername());
        return save;
    }

    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(QuoteExceptionCode.USER_NOT_FOUND));
        return modelMapper.map(user, UserProfileDto.class);
    }

    @Transactional
    public UserProfileDto updateUserProfile(UserProfileDto userProfileDto) {
        String username = userProfileDto.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(QuoteExceptionCode.USER_NOT_FOUND));

        Optional.ofNullable(userProfileDto.getName()).ifPresent(user::setName);
        Optional.ofNullable(userProfileDto.getEmail()).ifPresent(user::setEmail);

        return modelMapper.map(user, UserProfileDto.class);
    }

    @Transactional
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(QuoteExceptionCode.USER_NOT_FOUND));

        List<FolderDto.FolderResponse> folders = folderService.getFolders(user.getUsername());

        folderService.deleteFolders(folders.stream().map(FolderDto.FolderResponse::getFolderId).collect(Collectors.toList()));
        userRepository.deleteById(user.getId());
    }
}
