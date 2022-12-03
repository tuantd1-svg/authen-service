package com.example.authenservice.consumer;


import com.example.authenservice.service.UserService;
import com.example.commonapi.model.RegisterUser;
import com.example.commonapi.model.ResultMessage;
import com.example.commonapi.model.User;
import com.example.commonapi.parameter.enumable.EMessage;
import com.example.commonapi.parameter.enumable.ETransactionStatus;
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
    public ResultMessage<RegisterUser > addUserAuthentication(@Valid @Payload User user) {
        try {
            RegisterUser registerUser = userService.addUser(user);
            if (registerUser != null)
                return new ResultMessage(ETransactionStatus.SUCCESS.getStatus(), ETransactionStatus.SUCCESS.getMessage(), true, EMessage.EXECUTE, registerUser);
            return new ResultMessage(ETransactionStatus.NOT_FOUND.getStatus(), ETransactionStatus.NOT_FOUND.getMessage(), false, EMessage.INTERNAL_ERROR);
        } catch (Exception e) {
            return new ResultMessage(ETransactionStatus.PROCESSING_ERROR.getStatus(), ETransactionStatus.PROCESSING_ERROR.getMessage() + " - " + e.getMessage(), false, EMessage.INTERNAL_ERROR);
        }
    }

}
