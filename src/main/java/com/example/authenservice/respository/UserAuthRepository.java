package com.example.authenservice.respository;

import com.example.authenservice.respository.dto.UserAuth;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserAuthRepository extends MongoRepository<UserAuth,Long> {
    UserAuth findUserAuthByRefAndIsActive(String ref,Boolean isActive);
}
