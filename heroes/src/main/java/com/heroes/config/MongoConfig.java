package com.heroes.config;

import com.heroes.repository.HeroRepository;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackageClasses = HeroRepository.class)
public class MongoConfig {

    @Value("$(spring.data.mongodb.host}")
    private String host;

    @Value("$(spring.data.mongodb.port}")
    private String port;

    @Value("$(spring.data.mongodb.database}")
    private String database;

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoProperties mongoProperties = new MongoProperties();
        mongoProperties.setDatabase("marvel");
        mongoProperties.setHost("localhost");
        mongoProperties.setPort(27017);

        return new MongoTemplate(factory(mongoProperties));
    }

    @Bean
    public MongoDbFactory factory(final MongoProperties mongo) throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()),
                mongo.getDatabase());
    }

}
