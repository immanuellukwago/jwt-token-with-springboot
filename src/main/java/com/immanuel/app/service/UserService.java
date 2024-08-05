package com.immanuel.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.immanuel.app.model.login.LoginForm;
import com.immanuel.app.model.user.MyUser;
import com.immanuel.app.repository.MyUserRepository;
import com.immanuel.app.web.JwtService;

@Service
public class UserService {

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MyUserDetailsService userDetailsService;

    public void saveUser(MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(user.getRoles().toUpperCase());
        myUserRepository.save(user);
    }

    public MyUser findByUsername(String username) {
        Optional<MyUser> optional = myUserRepository.findByUsername(username);
        if (optional.isPresent()) {
            return optional.get();
        } else
            throw new UsernameNotFoundException(username + " cannot be found");
    }

    public String authenticateAndGetToken(LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(userDetailsService.loadUserByUsername(loginForm.getUsername()));
        } else
            throw new UsernameNotFoundException("Invalid Credentials!");
    }
}
