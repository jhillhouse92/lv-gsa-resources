package com.longview.gsa.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@SpringBootApplication
@ComponentScan("com.longview.gsa")
public class ApplicationConfig extends AbstractMongoConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfig.class, args);
    }

	@Override
	protected String getDatabaseName() {
		// TODO Auto-generated method stub
		return "fda";
	}

	@Override
	public Mongo mongo() throws Exception {
		// TODO Auto-generated method stub
		return new MongoClient("127.0.0.1", 27017);
	}
	
	@Override
	protected String getMappingBasePackage() {
		return "com.longview.gsa.domain";
	}

}