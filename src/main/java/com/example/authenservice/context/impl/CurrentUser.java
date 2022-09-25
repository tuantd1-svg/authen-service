package com.example.authenservice.context.impl;

import com.example.authenservice.context.ICurrentUser;
import com.example.authenservice.respository.UserRepository;
import com.example.commonapi.parameter.enumable.EUserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser implements ICurrentUser {

    @Autowired
    private UserRepository iUsersRepository;

    @Autowired
    private AuthenticationContext authenticationContext;

    @Override
    public String getLogin() {
        return authenticationContext.getAuthenticationContext().getName();
    }

    public String getRefLogin() {
        return iUsersRepository.findUsersByUsername(getLogin(), EUserStatus.ACTIVE.getStatus()).getRef();
    }

}
