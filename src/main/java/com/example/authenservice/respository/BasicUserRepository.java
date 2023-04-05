package com.example.authenservice.respository;

import com.example.authenservice.respository.dto.BasicUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BasicUserRepository extends MongoRepository<BasicUser,Long> {
}
