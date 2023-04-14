package com.example.authenservice.respository.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("BasicFunction")
public class BasicFunction {

    @Id
    @Transient
    public static final String SEQUENCE_NAME = "BasicUser-sequence";
    private Long id;
    private Long userId;
    private String allowFunction;

    private String allowService;
}
