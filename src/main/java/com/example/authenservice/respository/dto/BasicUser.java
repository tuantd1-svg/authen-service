package com.example.authenservice.respository.dto;


import com.example.commonapi.parameter.enumable.ERole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;


@Getter
@Setter
@Document("BasicUser")
public class BasicUser {
    @Id
    @Transient
    public static final String SEQUENCE_NAME = "BasicUser-sequence";
    private Long id;

    private String username;

    private String password;

    private List<String> allowIp;

    private Set<ERole> role;


}
