package com.springcore.constructorinjection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication(
		scanBasePackages = {"com.springcore.constructorinjection",
							"common.util"}
)
public class ConstructorinjectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConstructorinjectionApplication.class, args);
	}

}
