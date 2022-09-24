package com.example.authenservice.respository.dto;

import com.example.commonapi.parameter.enumable.ELanguage;
import com.example.commonapi.parameter.enumable.ESend;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Builder
@Data
@Document(value = "UserConfig")
public class UserConfig {
    @Id
    @Transient
    public static final String SEQUENCE_NAME = "userConfig-sequence";
    private Long id;
    private String ref;
    private ELanguage language;
    private ESend sender;
    @CreatedDate
    private Date createDate;


}
