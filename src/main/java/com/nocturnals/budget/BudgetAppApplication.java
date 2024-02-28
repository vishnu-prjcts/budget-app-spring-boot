package com.nocturnals.budget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class BudgetAppApplication{

	public static void main(String[] args) {
		SpringApplication.run(BudgetAppApplication.class, args);
	}
}
