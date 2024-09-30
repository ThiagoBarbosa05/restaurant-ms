package com.microservices.restaurant_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RestaurantMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantMsApplication.class, args);
	}

}
