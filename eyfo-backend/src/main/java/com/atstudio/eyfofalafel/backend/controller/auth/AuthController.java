package com.atstudio.eyfofalafel.backend.controller.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;

@RestController
@Slf4j
public class AuthController {
    
    @GetMapping("/token")
    public ResponseEntity<AuthResponseDto> token(@ApiIgnore HttpSession session) {
        return ResponseEntity.ok(new AuthResponseDto(session.getId()));
    }

    @PostMapping("/user/logout")
    public ResponseEntity logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/checkSession")
    public ResponseEntity checkSession(@ApiIgnore Authentication authentication) {
        log.info("Check session for user " + authentication.getName());
        return ResponseEntity.ok().build();
    }
}
