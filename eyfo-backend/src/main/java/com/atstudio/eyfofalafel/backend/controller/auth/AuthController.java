package com.atstudio.eyfofalafel.backend.controller.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;

import static java.util.Collections.singletonMap;

@RestController
@Slf4j
public class AuthController {
    
    @GetMapping("/token")
    public ResponseEntity token(@ApiIgnore HttpSession session) {
        return ResponseEntity.ok(singletonMap("token", session.getId()));
    }

    @PostMapping("/user/logout")
    public ResponseEntity logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/checkSession")
    public ResponseEntity checkSession() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        log.info("Check session for user " + ((UserDetails) principal).getUsername());
        return ResponseEntity.ok().build();
    }
}
