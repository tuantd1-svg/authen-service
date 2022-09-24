package com.example.authenservice.respository;

import com.example.authenservice.respository.dto.UserConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserConfigRepository extends MongoRepository<UserConfig,Long> {

}
