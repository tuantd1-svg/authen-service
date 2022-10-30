package com.example.authenservice.respository.dto;

import com.example.commonapi.model.AbstractTimestampEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@Document(value = "Users")
public class Users extends AbstractTimestampEntity {
    @Transient
    public static final String SEQUENCE_NAME = "user_sequence";
    @Id
    private Long id;
    @NotNull(message = "username is not null")
    private String username;
    private String ref;
    @Email
    private String email;
    private String mobileNo;
    private String password;
    private String fullName;
    private Date dob;
    private String gender;
    private int status;
    private String apartment;
    private String street;
    private String wardName;
    private String district;
    private String cityName;
    private String address;
    @LastModifiedDate
    private Date isLoginSuccess;
}
