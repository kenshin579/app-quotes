package kr.pe.advenoh.user.service;

import kr.pe.advenoh.admin.folder.domain.FolderDto;
import kr.pe.advenoh.admin.folder.service.FolderService;
import kr.pe.advenoh.common.constants.AppConstants;
import kr.pe.advenoh.common.exception.ApiException;
import kr.pe.advenoh.common.exception.ErrorCode;
import kr.pe.advenoh.user.domain.AccountDto;
import kr.pe.advenoh.user.domain.RoleRepository;
import kr.pe.advenoh.user.domain.RoleType;
import kr.pe.advenoh.user.domain.User;
import kr.pe.advenoh.user.domain.UserDto;
import kr.pe.advenoh.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final FolderService folderService;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public User registerNewUserAccount(AccountDto.SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByUsername(signUpRequestDto.getUsername())) {
            throw new ApiException(ErrorCode.ACCOUNT_USERNAME_IS_ALREADY_EXIST);
        }

        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new ApiException(ErrorCode.ACCOUNT_EMAIL_IS_ALREADY_EXIST);
        }

        //새로운 사용자 생성
        User user = signUpRequestDto.toEntity();
        user.addRoles(Arrays.asList(roleRepository.findByRoleType(RoleType.ROLE_USER)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.ACCOUNT_ROLE_NOT_FOUND,
                        RoleType.ROLE_USER.name()
                ))));

        User save = userRepository.save(user);
        folderService.createFolder(AppConstants.DEFAULT_FOLDER, user.getUsername());
        return save;
    }

    @Transactional(readOnly = true)
    public UserDto.UserProfileDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        return modelMapper.map(user, UserDto.UserProfileDto.class);
    }

    @Transactional
    public UserDto.UserProfileDto updateUserProfile(String username, UserDto.UserProfileDto userProfileDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        user.updateUser(userProfileDto.getName(), userProfileDto.getEmail());
        return modelMapper.map(user, UserDto.UserProfileDto.class);
    }

    @Transactional
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        List<FolderDto.FolderResponse> folders = folderService.getFolders(user.getUsername());

        folderService.deleteFolders(folders.stream().map(FolderDto.FolderResponse::getFolderId).collect(Collectors.toList()));
        userRepository.deleteById(user.getId());
    }
}
