package com.example.authenservice.respository.dto;


import org.springframework.data.mongodb.core.mapping.Document;

@Document("BasicUser")
public class BasicUser {
    private Long id;

    private String username;

    private String password;

    private String allowIp;



}
