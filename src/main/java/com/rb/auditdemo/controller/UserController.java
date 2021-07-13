package com.rb.auditdemo.controller;

import com.rb.auditdemo.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/greeting/user/{username}")
    public String greetingUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    @GetMapping("/newuser")
    public void addUser() {
        userService.addUser("jyz", 26, "male");
    }
}
