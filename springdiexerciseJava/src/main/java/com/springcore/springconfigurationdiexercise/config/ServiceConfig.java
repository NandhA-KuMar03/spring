package com.springcore.springconfigurationdiexercise.config;

import com.springcore.springconfigurationdiexercise.common.BikeService;
import com.springcore.springconfigurationdiexercise.common.CarService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Lazy
@Configuration
public class ServiceConfig {
    @Bean("bike")
    public BikeService bikeService(){
        return new BikeService();
    }

    @Bean("car")
    public CarService carService(){
        return new CarService();
    }
}
