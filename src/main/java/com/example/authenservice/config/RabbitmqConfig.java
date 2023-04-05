package com.example.authenservice.config;


import com.example.queuecommonapi.config.QueueConfig;
import org.springframework.amqp.core.*;
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
        return new DirectExchange(QueueConfig.E_NOTIFICATION_SEND);
    }

    @Bean
    Binding bindingSendMail() {
        return BindingBuilder.bind(queueSendMail()).to(exchangeMail()).with(QueueConfig.Q_NOTIFICATION_SEND);
    }
    @Bean
    Queue queueSendMail() {
        return new Queue(QueueConfig.Q_NOTIFICATION_SEND, false);
    }

    @Bean
    DirectExchange exchangeUser() {
        return new DirectExchange(QueueConfig.E_USER);
    }

    @Bean
    Binding bindingCreateUser() {
        return BindingBuilder.bind(queueCreateUser()).to(exchangeUser()).with(QueueConfig.R_CREATE_USER);
    }
    @Bean
    Queue queueCoreCreateUser() {
        return new Queue(QueueConfig.Q_CORE_CREATE_USER, false);
    }
    @Bean
    TopicExchange exchangeCoreUser() {
        return new TopicExchange(QueueConfig.E_CORE_USER);
    }
    @Bean
    Binding bindingCoreCreateUser() {
        return BindingBuilder.bind(queueCoreCreateUser()).to(exchangeCoreUser()).with(QueueConfig.R_CORE_CREATE_USER);
    }
    @Bean
    Queue queueCreateUser() {
        return new Queue(QueueConfig.Q_CREATE_USER, false);
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

