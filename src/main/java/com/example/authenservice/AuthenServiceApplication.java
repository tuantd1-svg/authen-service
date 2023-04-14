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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableMongoAuditing
@EnableCaching
@EnableEurekaClient
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
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
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

    @Bean
    @Profile({"ws-dev","default"})
    public static PropertySourcesPlaceholderConfigurer devProperties(){
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer
                = new PropertySourcesPlaceholderConfigurer();
        Resource[] resources = new ClassPathResource[ ]
                { new ClassPathResource( "endpoint/ws-dev.properties" ) };
        propertySourcesPlaceholderConfigurer.setLocations( resources );
        propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders( true );
        return propertySourcesPlaceholderConfigurer;
    }
    @Bean
    @Profile("ws-uat")
    public static PropertySourcesPlaceholderConfigurer uatProperties(){
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer
                = new PropertySourcesPlaceholderConfigurer();
        Resource[] resources = new ClassPathResource[ ]
                { new ClassPathResource( "endpoint/ws-uat.properties" ) };
        propertySourcesPlaceholderConfigurer.setLocations( resources );
        propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders( true );
        return propertySourcesPlaceholderConfigurer;
    }
    @Bean
    @Profile("ws-uat")
    public static PropertySourcesPlaceholderConfigurer pilotProperties(){
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer
                = new PropertySourcesPlaceholderConfigurer();
        Resource[] resources = new ClassPathResource[ ]
                { new ClassPathResource( "endpoint/ws-pilot.properties" ) };
        propertySourcesPlaceholderConfigurer.setLocations( resources );
        propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders( true );
        return propertySourcesPlaceholderConfigurer;
    }
    @Bean
    @Profile("ws-pro")
    public static PropertySourcesPlaceholderConfigurer proProperties(){
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer
                = new PropertySourcesPlaceholderConfigurer();
        Resource[] resources = new ClassPathResource[ ]
                { new ClassPathResource( "endpoint/ws-production.properties" ) };
        propertySourcesPlaceholderConfigurer.setLocations( resources );
        propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders( true );
        return propertySourcesPlaceholderConfigurer;
    }
}
