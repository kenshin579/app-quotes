package kr.pe.advenoh.quote.controller;

import kr.pe.advenoh.quote.exception.QuoteExceptionCode;
import kr.pe.advenoh.quote.model.dto.ApiResponseDto;
import kr.pe.advenoh.quote.model.dto.LoginRequestDto;
import kr.pe.advenoh.quote.model.dto.SignUpRequestDto;
import kr.pe.advenoh.quote.model.entity.User;
import kr.pe.advenoh.quote.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        log.debug("[signupdebug] registerUser :: signUpRequest : {}", signUpRequestDto);
        User registeredUser = userService.registerNewUserAccount(signUpRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(registeredUser.getUsername()).toUri();

        log.info("[signupdebug] location : {}", location);
        return ResponseEntity.created(location).body(new ApiResponseDto(true, QuoteExceptionCode.ACCOUNT_USER_REGISTERED_SUCCESS.getMessage()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsernameOrEmail(),
                        loginRequestDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
        return null;
    }
}
