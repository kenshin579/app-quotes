package kr.pe.advenoh.quote.web.controller;

import kr.pe.advenoh.quote.model.User;
import kr.pe.advenoh.quote.service.IUserService;
import kr.pe.advenoh.quote.web.dto.request.SignUpRequest;
import kr.pe.advenoh.quote.web.dto.response.ApiResponse;
import kr.pe.advenoh.quote.web.exception.QuoteExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

//    @Autowired
//    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        log.debug("[signupdebug] registerUser :: signUpRequest : {}", signUpRequest);

        final User registeredUser = userService.registerNewUserAccount(signUpRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(registeredUser.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, QuoteExceptionCode.ACCOUNT_USER_REGISTERED_SUCCESS.getMessage()));
    }
}
