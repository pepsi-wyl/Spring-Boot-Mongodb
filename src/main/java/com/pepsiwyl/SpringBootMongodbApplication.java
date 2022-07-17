package com.pepsiwyl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class SpringBootMongodbApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootMongodbApplication.class, args);
//        MongoTemplateTest.createCollection();
//        MongoTemplateTest.dropCollection();
//        MongoTemplateTest.insertDocument();
//        MongoTemplateTest.queryDocument();
//        MongoTemplateTest.updateDocument();
//        MongoTemplateTest.deleteDocument();
    }
}
