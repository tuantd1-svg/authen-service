package com.example.authenservice.model;

import com.example.commonapi.parameter.enumable.ERole;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ApiUser {
    private String username;

    private String password;

    private List<String> allowIp;

    private List<String> allowFunction;

    private Set<ERole> role;

    private String allowService;
}
