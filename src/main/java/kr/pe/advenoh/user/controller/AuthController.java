package kr.pe.advenoh.user.controller;

import kr.pe.advenoh.common.exception.ErrorCode;
import kr.pe.advenoh.common.model.dto.ApiResponseDto;
import kr.pe.advenoh.spring.security.JwtTokenProvider;
import kr.pe.advenoh.spring.security.UserPrincipal;
import kr.pe.advenoh.user.domain.User;
import kr.pe.advenoh.user.domain.AccountDto;
import kr.pe.advenoh.user.domain.JwtAuthenticationResponseDto;
import kr.pe.advenoh.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final IUserService userService;

    private final JwtTokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid AccountDto.SignUpRequestDto signUpRequestDto) {
        log.debug("[authdebug] registerUser :: signUpRequest : {}", signUpRequestDto);
        User registeredUser = userService.registerNewUserAccount(signUpRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(registeredUser.getUsername()).toUri();

        log.info("[authdebug] location : {}", location);
        return ResponseEntity.created(location).body(new ApiResponseDto(true, ErrorCode.ACCOUNT_USER_REGISTERED_SUCCESS.getMessage()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid AccountDto.LoginRequestDto loginRequestDto) {
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
