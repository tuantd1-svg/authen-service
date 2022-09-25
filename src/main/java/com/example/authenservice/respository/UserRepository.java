package com.example.authenservice.respository;

import com.example.authenservice.respository.dto.Users;
import com.example.commonapi.parameter.enumable.EStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<Users,Long> {
    Users findUsersByUsernameAndEmailAndMobileNo(String username,String email,String mobileNo);
    Users findUsersByUsername(String username,int status);
    Users findUsersByRefAndStatus(String ref, EStatus eStatus);

    Users findUsersByUsernameOrEmailOrMobileNo(String username,String email,String mobileNo);
}
