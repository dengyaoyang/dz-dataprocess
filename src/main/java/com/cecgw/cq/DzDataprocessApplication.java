package com.cecgw.cq;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.logging.Logger;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
public class DzDataprocessApplication {

	public static void main(String[] args) {
		SpringApplication.run(DzDataprocessApplication.class, args);
	}

}
