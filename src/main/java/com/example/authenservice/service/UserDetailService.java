package com.example.authenservice.service;

import com.example.authenservice.respository.UserAuthRepository;
import com.example.authenservice.respository.UserRepository;
import com.example.authenservice.respository.UserRoleRepository;
import com.example.authenservice.respository.dto.UserAuth;
import com.example.authenservice.respository.dto.Users;
import com.example.commonapi.model.ResultMessage;
import com.example.commonapi.model.UserLogin;
import com.example.commonapi.parameter.enumable.EMessage;
import com.example.commonapi.parameter.enumable.ERole;
import com.example.commonapi.parameter.enumable.EUserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;
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

        Optional<Users> usersOptional = Optional.ofNullable(userRepository.findUsersByUsername(username, EUserStatus.ACTIVE.getStatus()));
        Users users = usersOptional.orElseThrow(() -> new UsernameNotFoundException("user not active please contact admin"));
        Optional<UserAuth> authOptional = Optional.ofNullable(userAuthRepository.findUserAuthByRefAndIsActive(users.getRef(), Boolean.TRUE));
        UserAuth userAuth = authOptional.orElseThrow(() -> new UsernameNotFoundException("user authentication not active please contact admin"));

        Set<ERole> roleSet = userRoleRepository.getUserRolesByRef(users.getRef()).getRole();
        return UserDetailServiceImp.builder(users, roleSet, userAuth);
    }
}
