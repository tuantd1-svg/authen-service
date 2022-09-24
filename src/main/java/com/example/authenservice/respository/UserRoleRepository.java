package com.example.authenservice.respository;

import com.example.authenservice.respository.dto.UserRoles;
import com.example.commonapi.parameter.enumable.ERole;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Set;

public interface UserRoleRepository extends MongoRepository<UserRoles,Long> {
    UserRoles getUserRolesByRef(String ref);
}
