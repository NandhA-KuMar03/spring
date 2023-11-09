package com.springcore.constructorinjection.controller;

import com.springcore.constructorinjection.common.Coach;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

public class Controller {

    private Coach mycoach;

    public Controller(Coach mycoach){
        this.mycoach = mycoach;
    }

    public String DailyWorkout(){
        return mycoach.getDailyWorkout();
    }
}
