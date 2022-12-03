package com.example.authenservice.mapper;

import com.example.authenservice.respository.dto.UserAuth;
import com.example.commonapi.parameter.enumable.EUserAuth;
import com.example.commonapi.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthMapper {
    @Autowired
    private SequenceGeneratorService service;

    public UserAuth map(EUserAuth eUserAuth, String ref) {
        UserAuth userAuth = new UserAuth();
        userAuth.setId(service.generateSequence(UserAuth.SEQUENCE_NAME));
        userAuth.setRef(ref);
        userAuth.setIsResetPassword(false);
        switch (eUserAuth) {
            case ACTIVE:
                userAuth.setIsActive(true);
                userAuth.setIsDelete(false);
                userAuth.setIsLocked(false);
                break;
            case DELETE:
                userAuth.setIsLocked(true);
                userAuth.setIsDelete(true);
                userAuth.setIsActive(false);
                break;
            case LOCKED:
                userAuth.setIsActive(false);
                userAuth.setIsLocked(true);
                userAuth.setIsDelete(false);
                break;

        }
        return userAuth;
    }
}
