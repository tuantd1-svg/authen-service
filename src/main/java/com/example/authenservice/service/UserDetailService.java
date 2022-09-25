package com.example.authenservice.service;

import com.example.authenservice.respository.UserAuthRepository;
import com.example.authenservice.respository.UserRepository;
import com.example.authenservice.respository.UserRoleRepository;
import com.example.authenservice.respository.dto.UserAuth;
import com.example.authenservice.respository.dto.Users;
import com.example.commonapi.parameter.enumable.ERole;
import com.example.commonapi.parameter.enumable.EUserStatus;
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

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findUsersByUsername(username, EUserStatus.ACTIVE.getStatus());
        UserAuth auth = userAuthRepository.findUserAuthByRefAndIsActive(users.getRef(), Boolean.TRUE);
        if (auth != null && users != null) {
            Set<ERole> roleSet = userRoleRepository.getUserRolesByRef(users.getRef()).getRole();
            return UserDetailServiceImp.builder(users, roleSet ,auth);
        } else throw new UsernameNotFoundException("user not active please contact admin");

    }
}
