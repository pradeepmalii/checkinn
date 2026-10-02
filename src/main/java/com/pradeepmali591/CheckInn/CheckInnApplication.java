package com.pradeepmali591.CheckInn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CheckInnApplication {

	public static void main(String[] args) {
		SpringApplication.run(CheckInnApplication.class, args);
	}

}
