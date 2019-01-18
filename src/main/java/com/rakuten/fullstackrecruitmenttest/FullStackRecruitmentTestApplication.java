package com.rakuten.fullstackrecruitmenttest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.rakuten.fullstackrecruitmenttest.storage.StorageProperties;
import com.rakuten.fullstackrecruitmenttest.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class FullStackRecruitmentTestApplication {

    Logger logger = LoggerFactory.getLogger(FullStackRecruitmentTestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FullStackRecruitmentTestApplication.class, args);
	}

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
