package com.example.authenservice.service;

import com.example.authenservice.mapper.UserConfigMapper;
import com.example.authenservice.mapper.UserRolesMapper;
import com.example.authenservice.mapper.UsersMapper;
import com.example.authenservice.respository.UserConfigRepository;
import com.example.authenservice.respository.UserRepository;
import com.example.authenservice.respository.UserRoleRepository;
import com.example.authenservice.respository.dto.UserConfig;
import com.example.authenservice.respository.dto.UserRoles;
import com.example.authenservice.respository.dto.Users;
import com.example.commonapi.anotation.exeption.MyResourceNotFoundException;
import com.example.commonapi.anotation.exeption.ResourceExistException;
import com.example.commonapi.model.RegisterUser;
import com.example.commonapi.model.User;
import com.example.loggerapi.utils.LoggerUtils2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserConfigRepository userConfigRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private UserConfigMapper userConfigMapper;
    @Autowired
    private UserRolesMapper userRolesMapper;
    public RegisterUser addUser(User user) {
        Users temp = userRepository.findUsersByUsernameAndEmailAndMobileNo(user.getUsername(), user.getEmail(), user.getMobileNo());
        if (temp == null) {
            Users users = userRepository.save(usersMapper.map(user));
            UserConfig userConfig = userConfigRepository.save(userConfigMapper.map(users.getRef()));
            UserRoles userRoles = userRoleRepository.save(userRolesMapper.map(users.getRef()));
            return getRegisterUser(users, userConfig, userRoles);
        } else {
            throw new ResourceExistException("User is exist");
        }
    }
    private RegisterUser getRegisterUser(Users users,UserConfig userConfig ,UserRoles userRoles)
    {
        return RegisterUser.builder()
                .username(users.getUsername())
                .email(users.getEmail())
                .mobileNo(users.getMobileNo())
                .fullName(users.getFullName())
                .dob(users.getDob())
                .role(userRoles.getRole())
                .language(userConfig.getLanguage())
                .DefaultSendNotification(userConfig.getSender())
                .address(users.getAddress())
                .createdDate(users.getIsLoginSuccess())
                .build();
    }
}
