package com.example.authenservice.service;

import com.example.authenservice.respository.BasicFunctionRepository;
import com.example.authenservice.respository.BasicUserRepository;
import com.example.authenservice.respository.dto.BasicFunction;
import com.example.authenservice.respository.dto.BasicUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class BasicUserService implements UserDetailsService {

    @Autowired
    private BasicUserRepository basicUserRepository;
    @Autowired
    private BasicFunctionRepository basicFunctionRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<BasicUser> userOptional = Optional.ofNullable(basicUserRepository.findBasicUserByUsername(username));
        BasicUser basicUser = userOptional.orElseThrow(()-> new UsernameNotFoundException("user not active please contact admin"));
        Optional<List<BasicFunction>> optionalBasicFunction = Optional.ofNullable(basicFunctionRepository.findAllByUserId(basicUser.getId()));
        List<BasicFunction> basicFunction =  optionalBasicFunction.orElseThrow(()-> new UsernameNotFoundException("user not active please contact admin"));

        List<String> allowFunction = basicFunction.stream().map(t->t.getAllowFunction()).collect(Collectors.toList());
        return  BasicUserImp.builder(basicUser,basicUser.getRole(),allowFunction);

    }
}
