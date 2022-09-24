package com.example.authenservice;

import com.example.commonapi.config.SpringMongoConfig;
import com.example.commonapi.service.ReferenceService;
import com.example.commonapi.service.SequenceGeneratorService;
import com.example.queuecommonapi.config.Receiver;
import com.example.queuecommonapi.producer.IQueueProducer;
import com.example.queuecommonapi.producer.QueueProducer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableMongoAuditing
@EnableCaching
public class AuthenServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenServiceApplication.class, args);
    }
    @Bean("restMappingJackson2HttpMessageConverter")
    public MappingJackson2HttpMessageConverter restMappingJackson2HttpMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
    @Bean
    public SpringMongoConfig springMongoConfig() {
        return new SpringMongoConfig();
    }

    @Bean("receiver")
    public Receiver receiver() {
        return new Receiver();
    }

    @Bean
    public IQueueProducer iQueueProducer(@Qualifier("receiver") final Receiver receiver) {
        return new QueueProducer(receiver);
    }
    @Bean
    public SequenceGeneratorService service() {
        return new SequenceGeneratorService();
    }
    @Bean
    public ReferenceService referenceService() {
        return new ReferenceService();
    }
}
