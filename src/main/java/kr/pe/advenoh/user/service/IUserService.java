package kr.pe.advenoh.user.service;

import kr.pe.advenoh.user.domain.User;
import kr.pe.advenoh.user.domain.AccountDto;

public interface IUserService {
    User registerNewUserAccount(AccountDto.SignUpRequestDto signUpRequestDto);

    void deleteUser(String username);
}
