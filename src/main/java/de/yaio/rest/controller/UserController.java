package de.yaio.rest.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @RequestMapping("current")
    public Principal user(final Principal user) {
        return user;
    }
}
