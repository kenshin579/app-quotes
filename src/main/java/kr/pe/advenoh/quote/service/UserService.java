package kr.pe.advenoh.quote.service;

import kr.pe.advenoh.quote.exception.AppException;
import kr.pe.advenoh.quote.exception.QuoteExceptionCode;
import kr.pe.advenoh.quote.exception.UserAlreadyExistException;
import kr.pe.advenoh.quote.model.dto.SignUpRequestDto;
import kr.pe.advenoh.quote.model.entity.User;
import kr.pe.advenoh.quote.model.enums.RoleType;
import kr.pe.advenoh.quote.repository.RoleRepository;
import kr.pe.advenoh.quote.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper mapper;

    @Override
    public User registerNewUserAccount(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByUsername(signUpRequestDto.getUsername())) {
            throw new UserAlreadyExistException(QuoteExceptionCode.ACCOUNT_USERNAME_IS_ALREADY_EXIST.getMessage());
        }

        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new UserAlreadyExistException(QuoteExceptionCode.ACCOUNT_EMAIL_IS_ALREADY_EXIST.getMessage());
        }

        //새로운 사용자 생성
        User user = mapper.map(signUpRequestDto, User.class);
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(() -> {
            throw new AppException(QuoteExceptionCode.ACCOUNT_ROLE_IS_NOT_SET.getMessage());
        })));
        return userRepository.save(user);
    }
}
