package kr.pe.advenoh.quote.controller;

import kr.pe.advenoh.quote.exception.QuoteExceptionCode;
import kr.pe.advenoh.quote.model.dto.ApiResponseDto;
import kr.pe.advenoh.quote.model.dto.JwtAuthenticationResponseDto;
import kr.pe.advenoh.quote.model.dto.LoginRequestDto;
import kr.pe.advenoh.quote.model.dto.SignUpRequestDto;
import kr.pe.advenoh.quote.model.entity.User;
import kr.pe.advenoh.quote.service.IUserService;
import kr.pe.advenoh.quote.spring.security.JwtTokenProvider;
import kr.pe.advenoh.quote.spring.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@ModelAttribute @Valid SignUpRequestDto signUpRequestDto) {
        log.debug("[authdebug] registerUser :: signUpRequest : {}", signUpRequestDto);
        User registeredUser = userService.registerNewUserAccount(signUpRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(registeredUser.getUsername()).toUri();

        log.info("[authdebug] location : {}", location);
        return ResponseEntity.created(location).body(new ApiResponseDto(true, QuoteExceptionCode.ACCOUNT_USER_REGISTERED_SUCCESS.getMessage()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@ModelAttribute @Valid LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        log.debug("[authdebug] generated jwt : {}", jwt);
        return ResponseEntity.ok(new JwtAuthenticationResponseDto(jwt));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        log.info("[authdebug] userId : {}", userPrincipal.getUsername());
        //todo : invalidate token
        return new ResponseEntity<>(new ApiResponseDto(true, "logout successfully"), HttpStatus.OK);
    }
}
