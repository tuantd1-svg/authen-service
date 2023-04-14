package com.example.authenservice.service;

import com.example.authenservice.mapper.BasicUserMapper;
import com.example.authenservice.model.ApiUser;
import com.example.authenservice.respository.BasicFunctionRepository;
import com.example.authenservice.respository.BasicUserRepository;
import com.example.authenservice.respository.dto.BasicUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiUserService {
    @Autowired
    private BasicUserMapper basicUserMapper;
    @Autowired
    private BasicFunctionRepository basicFunctionRepository;

    @Autowired
    private BasicUserRepository basicUserRepository;

    public void createUserCallApi(ApiUser apiUser) {
        BasicUser basicUser = basicUserRepository.save(basicUserMapper.mapBasicUser(apiUser));

        basicFunctionRepository.saveAll(basicUserMapper.mapBasicFunction(apiUser, basicUser));
    }
}
