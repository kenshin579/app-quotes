package kr.pe.advenoh.user.service;

import kr.pe.advenoh.user.domain.User;
import kr.pe.advenoh.user.domain.dto.SignUpRequestDto;

public interface IUserService {
    User registerNewUserAccount(SignUpRequestDto signUpRequestDto);

    void deleteUser(String username);
}
