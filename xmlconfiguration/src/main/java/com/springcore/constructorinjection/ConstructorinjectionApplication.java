package com.springcore.constructorinjection;

import com.springcore.constructorinjection.controller.Controller;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class ConstructorinjectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConstructorinjectionApplication.class, args);
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
		Controller controller = applicationContext.getBean("controller", Controller.class);
		System.out.println(controller.DailyWorkout());
	}

}
