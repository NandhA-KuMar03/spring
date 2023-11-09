package com.springcore.constructorinjection.controller;


import common.util.common.Coach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    //defining for dependency
    private Coach myCoach;

    //Since Coach has only one implementation it can autowire automatically
    @Autowired
    public Controller(Coach theCoach){
        myCoach = theCoach;
    }

    @GetMapping("/dailyworkout")
    public String DailyWorkout(){
        return myCoach.getDailyWorkout();
    }

}
