package com.compro.controller;

import com.compro.model.User;
import com.compro.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/")
public class UserController {
    private static Logger logger;

    @Autowired
    private UserService userService;

    @PostMapping("register")
    public User register(@RequestParam
                            String username,
                         @RequestParam
                            String password,
                         @RequestParam
                             String name,
                         @RequestParam
                            String email,
                         @RequestParam
                             String level){
        logger.info("=====User Register Request=====");
        return userService.register(username,password,name,email,level);
    }

    @PostMapping("login")
    public String login(@RequestParam String username,
                        @RequestParam String password){
        logger.info("=====Login Request=====");
        return userService.login(username, password);
    }

    @GetMapping("access")
    public Boolean checkAccess(@RequestHeader("Authorization") String token,
                               @RequestParam String requiredLevel) {
        return userService.checkAccess(token.replace("Bearer ", ""), requiredLevel);
    }
}
