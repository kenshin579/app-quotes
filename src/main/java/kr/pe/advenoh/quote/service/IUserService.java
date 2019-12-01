package kr.pe.advenoh.quote.service;

import kr.pe.advenoh.quote.model.User;
import kr.pe.advenoh.quote.web.dto.request.SignUpRequest;

public interface IUserService {
    User registerNewUserAccount(SignUpRequest signUpRequest);
}
