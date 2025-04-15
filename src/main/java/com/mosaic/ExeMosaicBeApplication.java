package com.mosaic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ExeMosaicBeApplication {
	public static void main(String[] args) {
		SpringApplication.run(ExeMosaicBeApplication.class, args);
	}
}
