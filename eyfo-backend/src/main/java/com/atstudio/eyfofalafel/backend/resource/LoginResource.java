package com.atstudio.eyfofalafel.backend.resource;

import com.atstudio.eyfofalafel.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

@RestController
@Slf4j
public class LoginResource {

    private UserService userService;

    public LoginResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/token")
    public Map<String, String> token(HttpSession session, HttpServletRequest request) {
        log.info("Remote host and port: " + request.getRemoteHost() + ":" + request.getRemotePort());
        log.info("Remote address: " + request.getRemoteAddr());
        return Collections.singletonMap("token", session.getId());
    }

    @PostMapping("/user/logout")
    public ResponseEntity logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/checkSession")
    public ResponseEntity checkSession(Authentication authentication) {
        if (authentication != null) {
            log.info("Check session for user " + authentication.getName());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
