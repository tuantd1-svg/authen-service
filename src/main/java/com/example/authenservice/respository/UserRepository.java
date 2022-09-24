package com.example.authenservice.respository;

import com.example.authenservice.respository.dto.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<Users,Long> {
    Users findUsersByUsernameAndEmailAndMobileNo(String username,String email,String mobileNo);
    Users findUsersByUsername(String username,int status);
}
