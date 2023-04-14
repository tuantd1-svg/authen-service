package com.example.authenservice.mapper;

import com.example.authenservice.model.ApiUser;
import com.example.authenservice.respository.dto.BasicFunction;
import com.example.authenservice.respository.dto.BasicUser;
import com.example.commonapi.parameter.enumable.ERole;
import com.example.commonapi.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.util.CollectionUtils;

import javax.persistence.SequenceGenerator;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BasicUserMapper {
    @Autowired
    private SequenceGeneratorService service;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public BasicUser mapBasicUser(ApiUser apiUser)
    {
        BasicUser basicUser = new BasicUser();
        basicUser.setId(service.generateSequence(BasicUser.SEQUENCE_NAME));
        basicUser.setUsername(apiUser.getUsername());
        basicUser.setPassword(passwordEncoder.encode(apiUser.getPassword()));
        basicUser.setRole(CollectionUtils.isEmpty(apiUser.getRole())?new HashSet<>(Arrays.asList(ERole.ROLE_CALL_API)):apiUser.getRole());
        basicUser.setAllowIp(apiUser.getAllowIp());
        return basicUser;
    }
    public List<BasicFunction> mapBasicFunction(ApiUser apiUser, BasicUser basicUser)
    {
        return apiUser.getAllowFunction().stream().map(t -> {
                    BasicFunction basicFunction = new BasicFunction();
                    basicFunction.setId(service.generateSequence(BasicFunction.SEQUENCE_NAME));
                    basicFunction.setUserId(basicUser.getId());
                    basicFunction.setAllowFunction(t);
                    basicFunction.setAllowService(apiUser.getAllowService());
                    return basicFunction;
                }
        ).collect(Collectors.toList());
    }
}
