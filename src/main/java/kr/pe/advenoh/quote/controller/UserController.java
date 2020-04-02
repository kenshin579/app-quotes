package kr.pe.advenoh.quote.controller;

import kr.pe.advenoh.quote.exception.ResourceNotFoundException;
import kr.pe.advenoh.quote.model.dto.UserInfoDto;
import kr.pe.advenoh.quote.model.entity.User;
import kr.pe.advenoh.quote.repository.UserRepository;
import kr.pe.advenoh.quote.spring.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public UserInfoDto getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return new UserInfoDto(userPrincipal.getId(), userPrincipal.getUsername(), userPrincipal.getName());

//        return userRepository.findById(userPrincipal.getId())
//                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
