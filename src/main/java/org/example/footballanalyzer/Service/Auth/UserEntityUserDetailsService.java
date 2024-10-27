package org.example.footballanalyzer.Service.Auth;

import lombok.RequiredArgsConstructor;
import org.example.footballanalyzer.Data.Entity.UserEntity;
import org.example.footballanalyzer.Data.UserEntityUserDetails;
import org.example.footballanalyzer.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserEntityUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByLogin(username);
        return userEntity.map(UserEntityUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }
}
