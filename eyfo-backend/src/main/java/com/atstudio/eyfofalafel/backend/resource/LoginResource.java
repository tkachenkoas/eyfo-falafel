package com.atstudio.eyfofalafel.backend.resource;

import com.atstudio.eyfofalafel.backend.service.UserService;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;

@RestController
public class LoginResource {

    private static Logger log;
    private UserService userService;

    public LoginResource(Logger logger, UserService userService) {
        this.log = logger;
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
    public ResponseEntity checkSession(Principal principal) {
        log.info("Check session for user " + principal.getName());
        return ResponseEntity.ok().build();
    }
}
