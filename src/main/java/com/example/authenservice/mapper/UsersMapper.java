package com.example.authenservice.mapper;


import com.example.authenservice.respository.dto.Users;
import com.example.commonapi.model.Address;
import com.example.commonapi.model.User;
import com.example.commonapi.parameter.enumable.EStatus;
import com.example.commonapi.parameter.enumable.EUserStatus;
import com.example.commonapi.service.ReferenceService;
import com.example.commonapi.service.SequenceGeneratorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsersMapper {
    @Autowired
    private SequenceGeneratorService service;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ReferenceService referenceService;
    public Users map(User user)
    {

        Users users = Users.builder()
                .id(service.generateSequence(Users.SEQUENCE_NAME))
                .ref(referenceService.newReference())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .mobileNo(user.getMobileNo())
                .dob(user.getDob())
                .gender(user.getGender())
                .apartment(user.getAddress().getApartment())
                .street(user.getAddress().getStreet())
                .wardName(user.getAddress().getWardName())
                .district(user.getAddress().getDistrict())
                .cityName(user.getAddress().getCityName())
                .address(user.getAddress().getApartment()+"-"+ user.getAddress().getStreet()+"-"+ user.getAddress().getWardName()+"-"+ user.getAddress().getDistrict())
                .status(EUserStatus.ACTIVE.getStatus()).build();
        return users;
    }
    public User map2User(Users users)
    {
        return User.builder()
                .username(users.getUsername())
                .email(users.getEmail())
                .password(users.getPassword())
                .gender(users.getGender())
                .mobileNo(users.getMobileNo())
                .fullName(users.getFullName())
                .dob(users.getDob())
                .address(modelMapper.map(users, Address.class))
                .build();
    }
}
