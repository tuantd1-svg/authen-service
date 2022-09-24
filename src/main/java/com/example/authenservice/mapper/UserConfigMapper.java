package com.example.authenservice.mapper;

import com.example.authenservice.respository.dto.UserConfig;
import com.example.commonapi.model.User;
import com.example.commonapi.parameter.enumable.ELanguage;
import com.example.commonapi.parameter.enumable.ESend;
import com.example.commonapi.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserConfigMapper {
    @Autowired
    private SequenceGeneratorService service;
    public UserConfig map (String refUser)
    {
        return UserConfig.builder()
                .id(service.generateSequence(UserConfig.SEQUENCE_NAME))
                .ref(refUser)
                .sender(ESend.EMAIL)
                .language(ELanguage.VI)
                .build();
    }
}
