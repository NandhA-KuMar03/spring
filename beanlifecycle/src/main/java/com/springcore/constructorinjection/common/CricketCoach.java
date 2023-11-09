package com.springcore.constructorinjection.common;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class CricketCoach implements Coach{

    //init method
    @PostConstruct
    public void StartUpStuff(){
        System.out.println("Bean Created for Cricket Coach");
    }

    //destroy method
    @PreDestroy
    public void EndingStuff(){
        System.out.println("After Bean destroyed");
    }
    @Override
    public String getDailyWorkout() {
        return "Practice fast bowling!!!!";
    }
}
