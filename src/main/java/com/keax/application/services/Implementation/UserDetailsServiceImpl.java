package com.keax.application.services.Implementation;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import com.keax.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepositoryPort userRepositoryPort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var userModel = userRepositoryPort.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Incorrect username or password")
        );

        return User.withUsername(userModel.getUsername())
                .password(userModel.getPassword())
                .authorities(new ArrayList<>())
                .build();
    }

}
