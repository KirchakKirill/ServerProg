package com.example.GameREST;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class GameRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameRestApplication.class, args);
	}




}

