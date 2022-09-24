package com.example.authenservice.config;


import com.example.queuecommonapi.config.QueueConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitmqConfig {

    @Bean
    DirectExchange exchangeMail() {
        return new DirectExchange(QueueConfig.E_MAIL_SEND);
    }

    @Bean
    Binding bindingSendMail() {
        return BindingBuilder.bind(queueSendMail()).to(exchangeMail()).with(QueueConfig.R_MAIL_SEND);
    }
    @Bean
    Queue queueSendMail() {
        return new Queue(QueueConfig.Q_MAIL_SEND, false);
    }
    @Bean
    Queue queueAuthorizeUser() {
        return new Queue(QueueConfig.Q_AUTHORIZE_USER, false);
    }

    @Bean
    Binding bindingAuthorizeUser() {
        return BindingBuilder.bind(queueAuthorizeUser()).to(exChangeUserShop()).with(QueueConfig.R_AUTHORIZE_USER);
    }
    @Bean
    Queue queueCheckUser() {
        return new Queue(QueueConfig.Q_CHECK_SHOP_USER, false);
    }

    @Bean
    Binding bindingCheckUser() {
        return BindingBuilder.bind(queueCheckUser()).to(exChangeUserShop()).with(QueueConfig.R_CHECK_SHOP_USER);
    }
    @Bean
    Queue queueCreateUser() {
        return new Queue(QueueConfig.Q_CREATE_SHOP_USER, false);
    }

    @Bean
    Binding bindingCreateUser() {
        return BindingBuilder.bind(queueCreateUser()).to(exChangeUserShop()).with(QueueConfig.R_CREATE_SHOP_USER);
    }
    @Bean
    Queue queueGetUserShop() {
        return new Queue(QueueConfig.Q_GET_SHOP_USER, false);
    }

    @Bean
    Binding bindingGetUsersShop() {
        return BindingBuilder.bind(queueGetUserShop()).to(exChangeUserShop()).with(QueueConfig.R_GET_SHOP_USER);
    }

    @Bean
    DirectExchange exChangeUserShop() {
        return new DirectExchange(QueueConfig.E_SHOP_USER);
    }

    @Bean
    Queue queueChangePassUser() {
        return new Queue(QueueConfig.Q_CHANGE_PASSWORD_USER, false);
    }

    @Bean
    Binding bindingChangePassUser() {
        return BindingBuilder.bind(queueChangePassUser()).to(exChangeUserShop()).with(QueueConfig.R_CHANGE_PASSWORD_USER);
    }


    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory jsaFactory(ConnectionFactory connectionFactory,
                                                           SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }
}

