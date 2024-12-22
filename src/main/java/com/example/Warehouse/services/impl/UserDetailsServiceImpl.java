package com.example.Warehouse.services.impl;

import com.example.Warehouse.domain.repositories.contracts.user.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepo;
    private static final Logger LOG = LogManager.getLogger(Service.class);

    public UserDetailsServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOG.info("loadUserByUsername called, username - {}", username);

        return userRepo.findByUsername(username)
            .map(u -> new User(
                u.getUsername(),
                u.getPasswordHash(),
                u.getRoles().stream()
                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName().name()))
                    .collect(Collectors.toList())
            )).orElseThrow(() -> new UsernameNotFoundException(username + " was not found!"));
    }
}
