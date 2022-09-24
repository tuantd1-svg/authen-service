package com.example.authenservice.mapper;

import com.example.authenservice.respository.dto.UserRoles;
import com.example.commonapi.parameter.enumable.ERole;
import com.example.commonapi.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserRolesMapper {
    @Autowired
    private SequenceGeneratorService service;
    public UserRoles map(String ref)
    {
        Set<ERole> roles = new HashSet<>();
        roles.add(ERole.ROLE_CUSTOMER);
        return UserRoles.builder()
                .id(service.generateSequence(UserRoles.SEQUENCE_NAME))
                .ref(ref)
                .role(roles)
                .build();
    }
}
