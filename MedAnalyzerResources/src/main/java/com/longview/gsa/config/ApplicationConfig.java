package com.longview.gsa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.longview.gsa.exception.OpenFdaExceptionHandler;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@SpringBootApplication
@EnableWebMvc
@Configuration
@ComponentScan("com.longview.gsa")
@PropertySource("classpath:gsa.properties")
public class ApplicationConfig extends AbstractMongoConfiguration {
	
	@Value("${mongo.host}")
	private String mongoHost;
	
	@Value("${mongo.port}")
	private int mongoPort;
	
    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfig.class, args);
    }

	@Override
	protected String getDatabaseName() {
		return "fda";
	}

	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient(mongoHost, mongoPort);
	}
	
	@Override
	protected String getMappingBasePackage() {
		return "com.longview.gsa.domain";
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate() {{
			setErrorHandler(new OpenFdaExceptionHandler());
		}};
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
	    return new PropertySourcesPlaceholderConfigurer();
	}
}