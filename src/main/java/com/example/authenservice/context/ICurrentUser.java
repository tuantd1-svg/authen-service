package com.example.authenservice.context;

import com.example.commonapi.parameter.enumable.ERole;

import java.util.Set;

public interface ICurrentUser {
    String getLogin();

    String getRefLogin();

     Set<ERole> getRole();

}
