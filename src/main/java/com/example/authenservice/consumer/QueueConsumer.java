package com.example.authenservice.consumer;


import com.example.authenservice.service.UserService;
import com.example.commonapi.anotation.exeption.FailureException;
import com.example.commonapi.model.RegisterUser;
import com.example.commonapi.model.ResultMessage;
import com.example.commonapi.model.User;
import com.example.commonapi.parameter.enumable.EMessage;
import com.example.commonapi.parameter.enumable.ETransactionStatus;
import com.example.commonapi.parameter.enumable.ErrorCode;
import com.example.queuecommonapi.config.QueueConfig;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class QueueConsumer {
    @Autowired
    private UserService userService;

    @RabbitHandler
    @RabbitListener(queues = QueueConfig.Q_CREATE_USER)
    public ResultMessage<?> addUserAuthentication(@Valid @Payload User user) {
        return userService.addUser(user);
    }
}
