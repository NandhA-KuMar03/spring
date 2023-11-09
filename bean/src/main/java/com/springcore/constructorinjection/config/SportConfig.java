package com.springcore.constructorinjection.config;


import com.springcore.constructorinjection.common.Coach;
import com.springcore.constructorinjection.common.SwimCoach;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SportConfig {

    @Bean
    public Coach swimCoach(){
        return new SwimCoach();
    }
}
