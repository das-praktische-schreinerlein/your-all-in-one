package de.yaio.app.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @RequestMapping("current")
    public Principal user(final Principal user) {
        return user;
    }
}
