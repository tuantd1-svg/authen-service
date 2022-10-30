package com.example.authenservice.producer;

import com.example.authenservice.context.ICurrentUser;
import com.example.commonapi.model.*;
import com.example.queuecommonapi.config.QueueConfig;
import com.example.queuecommonapi.producer.IQueueProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ShopAdminService {
    @Autowired
    private IQueueProducer iQueueProducer;
    @Autowired
    private ICurrentUser iCurrentUser;

    public ResultMessage createOrUpdateCategory(CategoryDto categoryDto) {
        categoryDto.setCreateUser(iCurrentUser.getLogin());
        return iQueueProducer.blockingStartRPCQueue(QueueConfig.Q_CREATE_CATEGORY, categoryDto);
    }

    public ResultMessage createOrUpdateProduct(ProductDto productDto) {
        productDto.setCreateUser(iCurrentUser.getLogin());
        return iQueueProducer.blockingStartRPCQueue(QueueConfig.Q_CREATE_PRODUCT, productDto);
    }
}
