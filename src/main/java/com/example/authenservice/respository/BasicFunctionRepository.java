package com.example.authenservice.respository;

import com.example.authenservice.respository.dto.BasicFunction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BasicFunctionRepository extends MongoRepository<BasicFunction, Long> {
    List<BasicFunction> findAllByUserId(Long id);
}
