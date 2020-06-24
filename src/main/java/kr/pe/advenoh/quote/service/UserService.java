package kr.pe.advenoh.quote.service;

import kr.pe.advenoh.quote.exception.ApiException;
import kr.pe.advenoh.quote.exception.QuoteExceptionCode;
import kr.pe.advenoh.quote.model.dto.FolderResponseDto;
import kr.pe.advenoh.quote.model.dto.SignUpRequestDto;
import kr.pe.advenoh.quote.model.dto.UserProfileDto;
import kr.pe.advenoh.quote.model.entity.User;
import kr.pe.advenoh.quote.model.enums.RoleType;
import kr.pe.advenoh.quote.repository.RoleRepository;
import kr.pe.advenoh.quote.repository.UserRepository;
import kr.pe.advenoh.quote.util.AppConstants;
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
        user.setRoles(Arrays.asList(roleRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(() -> new ApiException(QuoteExceptionCode.ACCOUNT_ROLE_IS_NOT_SET))));

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

        List<FolderResponseDto> folders = folderService.getFolders(user.getUsername());

        folderService.deleteFolders(folders.stream().map(FolderResponseDto::getFolderId).collect(Collectors.toList()));
        userRepository.deleteById(user.getId());
    }
}
