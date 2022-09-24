package com.example.authenservice.service;

import com.example.authenservice.respository.UserRepository;
import com.example.authenservice.respository.UserRoleRepository;
import com.example.authenservice.respository.dto.Users;

import com.example.commonapi.parameter.enumable.ERole;
import com.example.commonapi.parameter.enumable.EUserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserDetailService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findUsersByUsername(username, EUserStatus.ACTIVE.getStatus());
        if (users != null) {
            Set<ERole> roleSet = userRoleRepository.getUserRolesByRef(users.getRef()).getRole();
            return UserDetailServiceImp.builder(users, roleSet);
        } else throw new UsernameNotFoundException("user not active please contact admin");

    }
}
