package kr.pe.advenoh.quote.service;

import kr.pe.advenoh.quote.model.dto.SignUpRequestDto;
import kr.pe.advenoh.quote.model.entity.User;

public interface IUserService {
    User registerNewUserAccount(SignUpRequestDto signUpRequestDto);

    void deleteUser(String username);
}
