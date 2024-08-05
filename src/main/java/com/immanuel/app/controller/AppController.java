package com.immanuel.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.immanuel.app.model.login.LoginForm;
import com.immanuel.app.model.user.MyUser;
import com.immanuel.app.service.UserService;

@RestController
public class AppController {

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String getHomePage() {
        return "This is the home page!";
    }

    @GetMapping("/user")
    public String getUserPage() {
        return "This is the user page!";
    }

    @GetMapping("/admin")
    public String getAdminPage() {
        return "This is the admin page!";
    }

    @PostMapping("/register/user")
    public MyUser registerUser(@RequestBody MyUser user) {
        userService.saveUser(user);
        return userService.findByUsername(user.getUsername());
    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody LoginForm loginForm) {
        return userService.authenticateAndGetToken(loginForm);
    }

}
