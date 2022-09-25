package com.example.authenservice.respository.dto;

import com.example.commonapi.model.AbstractTimestampEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(value = "UserAuth")
public class UserAuth extends AbstractTimestampEntity {
    @Id
    @Transient
    public static final String SEQUENCE_NAME = "UserAuth-sequence";
    private Long id;
    private String ref;
    private Boolean isActive;
    private Boolean isLocked;
    private Boolean isDelete;
}
