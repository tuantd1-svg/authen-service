package com.example.authenservice.respository.dto;


import com.example.commonapi.model.AbstractTimestampEntity;
import com.example.commonapi.parameter.enumable.EAuthResource;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Builder
@Data
@Document(value = "AuthResource")
public class AuthResource extends AbstractTimestampEntity {
    @Id
    @Transient
    public static final String SEQUENCE_NAME = "auth-sequence";
    private Long id;
    private String ref;
    private String token;
    private String refCommon;
    private String credential;
    private int count;
    private EAuthResource authResource;
    private Date expireDate;
    private Integer status;

}
