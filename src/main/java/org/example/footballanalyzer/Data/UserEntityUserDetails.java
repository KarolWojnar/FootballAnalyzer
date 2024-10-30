package org.example.footballanalyzer.Data;

import org.example.footballanalyzer.Data.Entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserEntityUserDetails implements UserDetails {
    private final String login;
    private final String password;
    private final boolean isEnable;
    private final List<GrantedAuthority> authorities;

    public UserEntityUserDetails(UserEntity userEntity) {
        login = userEntity.getLogin();
        password = userEntity.getPassword();
        authorities = Collections.singletonList(new SimpleGrantedAuthority(userEntity.getRole().getRoleName().name()));
        isEnable = userEntity.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnable;
    }
}
