package com.immanuel.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.immanuel.app.model.user.MyUser;
import com.immanuel.app.repository.MyUserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private MyUserRepository myUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> optional = myUserRepository.findByUsername(username);
        if (optional.isPresent()) {
            return User.builder()
                    .username(optional.get().getUsername())
                    .password(optional.get().getPassword())
                    .roles(getRoles(optional.get()))
                    .build();
        } else
            throw new UsernameNotFoundException(username + " is not found!");
    }

    private String[] getRoles(MyUser user) {
        if (user.getRoles() == null)
            return new String[] { "USER" };
        else
            return user.getRoles().split(",");
    }
}
