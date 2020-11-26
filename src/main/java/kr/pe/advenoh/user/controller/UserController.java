package kr.pe.advenoh.user.controller;

import kr.pe.advenoh.spring.security.UserPrincipal;
import kr.pe.advenoh.user.domain.dto.UserProfileDto;
import kr.pe.advenoh.user.domain.dto.UserResponseDto;
import kr.pe.advenoh.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final ModelMapper modelMapper;

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public UserResponseDto getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return new UserResponseDto(userPrincipal.getId(), userPrincipal.getUsername(), userPrincipal.getName());
    }

    @GetMapping("/{username}")
    public UserProfileDto getUserProfile(@PathVariable(value = "username") String username) {
        return userService.getUserProfile(username);
    }

    @PutMapping("/{username}")
    public UserProfileDto changeUserProfile(
            @PathVariable(value = "username") String username,
            @ModelAttribute UserProfileDto userProfileDto) {
        userProfileDto.setUsername(username);
        return userService.updateUserProfile(userProfileDto);
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable(value = "username") String username) {
        userService.deleteUser(username);
    }
}