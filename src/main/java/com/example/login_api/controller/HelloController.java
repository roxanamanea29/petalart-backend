package com.example.login_api.controller;

import com.example.login_api.security.UserPrincipal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {

        @GetMapping("/home")
        public String gretting() {
            return "Hello, Roxy";
        }

        @GetMapping("/secured")
        public String secured(@AuthenticationPrincipal UserPrincipal principal) {
            return "Hello, Secured! IF you dee this that means that it works,and you are authenticated! "
                    + principal.getEmail() + ". "+ " user id: " + principal.getUserId();
    }

    @GetMapping("/admin")
    public String admin(@AuthenticationPrincipal UserPrincipal principal) {
        return "Hello, Admin! IF you dee this that means that it works,and you are authenticated! "
                + principal.getEmail() + ". "+ " user id: " + principal.getUserId();
    }
}
