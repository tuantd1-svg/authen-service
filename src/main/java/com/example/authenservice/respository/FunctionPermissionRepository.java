package com.example.authenservice.respository;

import com.example.authenservice.respository.dto.FunctionPermission;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FunctionPermissionRepository extends MongoRepository<FunctionPermission,Long> {
    FunctionPermission findFunctionPermissionByUri(String uri);
}
