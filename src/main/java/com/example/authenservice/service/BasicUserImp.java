package com.example.authenservice.service;

import com.example.authenservice.respository.dto.BasicFunction;
import com.example.authenservice.respository.dto.BasicUser;
import com.example.authenservice.respository.dto.UserAuth;
import com.example.authenservice.respository.dto.Users;
import com.example.commonapi.parameter.enumable.ERole;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Data
@Builder
public class BasicUserImp implements UserDetails {
    private static final Long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String password;

    private List<String> allowFunction;




    private Collection<? extends GrantedAuthority> authorities;

    public static BasicUserImp builder(BasicUser users, Set<ERole> eRoles, List<String> function) {
        List<GrantedAuthority> authorities = eRoles.stream().map(roles -> new SimpleGrantedAuthority(roles.name())).collect(Collectors.toList());
        return new BasicUserImp(users.getId(), users.getUsername(), users.getPassword(),function, authorities);
    }

    public List<String> getAllowFunction() {
        return allowFunction;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true;
    }
}
