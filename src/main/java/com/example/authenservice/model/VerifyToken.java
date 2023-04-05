package com.example.authenservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class VerifyToken {
    private String uri;
    private boolean isVerifySuccess;
}
