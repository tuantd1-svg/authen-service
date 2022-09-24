package com.example.authenservice.respository.dto;

import com.example.commonapi.parameter.enumable.ERole;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Builder
@Data
@Document(value = "UserRole")
public class UserRoles {
    @Id
    @Transient
    public static final String SEQUENCE_NAME = "UserRole_sequence";
    private Long id;
    private String ref;
    private Set<ERole> role;
}
