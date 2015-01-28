package de.yaio.rest.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    
//    public class YAIODummyUser implements Principal {
//        @Override
//        public String getName() {
//            return "YAIODummyUser";
//        }
//        
//    }
    
    @RequestMapping("current")
    public Principal user(Principal user) {
//        user = new YAIODummyUser();
        return user;
    }
}
