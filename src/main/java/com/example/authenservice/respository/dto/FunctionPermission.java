package com.example.authenservice.respository.dto;

import com.example.commonapi.parameter.enumable.ERole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Set;

@Getter
@Setter
@Document(value = "FunctionPermission")
public class FunctionPermission {

    @Id
    @Transient
    public static final String SEQUENCE_NAME = "function-sequence";
    private Long id;
    private String uri;

    private Set<ERole> role;

    private String allowIp;


}
