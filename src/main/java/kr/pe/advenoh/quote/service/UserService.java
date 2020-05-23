package kr.pe.advenoh.quote.service;

import kr.pe.advenoh.quote.exception.AppException;
import kr.pe.advenoh.quote.exception.QuoteExceptionCode;
import kr.pe.advenoh.quote.exception.ResourceNotFoundException;
import kr.pe.advenoh.quote.exception.UserAlreadyExistException;
import kr.pe.advenoh.quote.model.dto.SignUpRequestDto;
import kr.pe.advenoh.quote.model.dto.UserProfileDto;
import kr.pe.advenoh.quote.model.entity.User;
import kr.pe.advenoh.quote.model.enums.RoleType;
import kr.pe.advenoh.quote.repository.RoleRepository;
import kr.pe.advenoh.quote.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    @Override
    public User registerNewUserAccount(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByUsername(signUpRequestDto.getUsername())) {
            throw new UserAlreadyExistException(QuoteExceptionCode.ACCOUNT_USERNAME_IS_ALREADY_EXIST.getMessage());
        }

        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new UserAlreadyExistException(QuoteExceptionCode.ACCOUNT_EMAIL_IS_ALREADY_EXIST.getMessage());
        }

        //새로운 사용자 생성
        User user = modelMapper.map(signUpRequestDto, User.class);
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(() -> {
            throw new AppException(QuoteExceptionCode.ACCOUNT_ROLE_IS_NOT_SET.getMessage());
        })));
        return userRepository.save(user);
    }

    public UserProfileDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return modelMapper.map(user, UserProfileDto.class);
    }

    @Transactional
    public UserProfileDto updateUserProfile(UserProfileDto userProfileDto) {
        String username = userProfileDto.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Optional.ofNullable(userProfileDto.getName()).ifPresent(user::setName);
        Optional.ofNullable(userProfileDto.getEmail()).ifPresent(user::setEmail);

        return modelMapper.map(user, UserProfileDto.class);
    }
}
